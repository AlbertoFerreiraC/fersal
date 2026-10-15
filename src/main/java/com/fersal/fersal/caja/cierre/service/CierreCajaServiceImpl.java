package com.fersal.fersal.caja.cierre.service;

import com.fersal.fersal.caja.cierre.dto.AperturaCajaRequest;
import com.fersal.fersal.caja.cierre.dto.ArqueoCajaRequest;
import com.fersal.fersal.caja.cierre.dto.CierreCajaRequest;
import com.fersal.fersal.caja.cierre.dto.CierreCajaResponse;
import com.fersal.fersal.caja.cierre.entity.CierreCaja;
import com.fersal.fersal.caja.cierre.entity.EstadoCaja;
import com.fersal.fersal.caja.cierre.mapper.CierreCajaMapper;
import com.fersal.fersal.caja.cierre.repository.CierreCajaRepository;
import com.fersal.fersal.caja.movimiento.repository.MovimientoCajaRepository;
import com.fersal.fersal.empresa.entity.Empresa;
import com.fersal.fersal.empresa.repository.EmpresaRepository;
import com.fersal.fersal.exception.BusinessException;
import com.fersal.fersal.exception.ResourceNotFoundException;
import com.fersal.fersal.notification.service.NotificationService;
import com.fersal.fersal.security.entity.Usuario;
import com.fersal.fersal.security.repository.UsuarioRepository;
import com.fersal.fersal.sucursal.entity.Sucursal;
import com.fersal.fersal.sucursal.repository.SucursalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CierreCajaServiceImpl implements CierreCajaService {

    private final CierreCajaRepository repository;
    private final MovimientoCajaRepository movimientoRepository;
    private final EmpresaRepository empresaRepository;
    private final SucursalRepository sucursalRepository;
    private final UsuarioRepository usuarioRepository;
    private final CierreCajaMapper mapper;
    private final NotificationService notificationService;

    private String formatoGs(BigDecimal monto) {
        if (monto == null) monto = BigDecimal.ZERO;
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("es", "PY"));
        symbols.setGroupingSeparator('.');
        DecimalFormat df = new DecimalFormat("#,##0", symbols);
        return "Gs. " + df.format(monto);
    }

    @Override
    @Transactional
    public CierreCajaResponse abrir(AperturaCajaRequest request) {
        boolean yaAbierta = repository.existsByEmpresaIdAndSucursalIdAndUsuarioIdAndEstado(
                request.getEmpresaId(),
                request.getSucursalId(),
                request.getUsuarioId(),
                EstadoCaja.ABIERTA
        );

        if (yaAbierta) {
            throw new BusinessException("El usuario ya cuenta con una caja abierta en esta sucursal");
        }

        Empresa empresa = empresaRepository.findById(request.getEmpresaId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada"));
        Sucursal sucursal = sucursalRepository.findById(request.getSucursalId())
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada"));
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        BigDecimal saldoInicial = request.getSaldoInicial() != null ? request.getSaldoInicial() : BigDecimal.ZERO;

        CierreCaja caja = CierreCaja.builder()
                .empresa(empresa)
                .sucursal(sucursal)
                .usuario(usuario)
                .saldoInicial(saldoInicial)
                .totalIngresos(BigDecimal.ZERO)
                .totalEgresos(BigDecimal.ZERO)
                .saldoFinal(saldoInicial)
                .estado(EstadoCaja.ABIERTA)
                .fechaApertura(LocalDateTime.now())
                .fecha(LocalDateTime.now())
                .build();

        CierreCaja guardado = repository.save(caja);

        notificationService.enviarPush(
                usuario.getFcmToken(),
                "Turno de Caja Abierto",
                "Se habilitó la caja en " + sucursal.getNombre() + " con base de " + formatoGs(saldoInicial)
        );

        return mapper.toResponse(guardado);
    }

    @Override
    @Transactional
    public CierreCajaResponse cerrar(CierreCajaRequest request) {
        CierreCaja caja = repository.findByEmpresaIdAndSucursalIdAndUsuarioIdAndEstado(
                request.getEmpresaId(),
                request.getSucursalId(),
                request.getUsuarioId(),
                EstadoCaja.ABIERTA
        ).orElseThrow(() -> new BusinessException("No existe una caja abierta para cerrar en esta sucursal"));

        BigDecimal ingresos = movimientoRepository.sumBySucursalIdAndTipo(request.getSucursalId(), "INGRESO");
        if (ingresos == null) ingresos = BigDecimal.ZERO;

        BigDecimal egresos = movimientoRepository.sumBySucursalIdAndTipo(request.getSucursalId(), "SALIDA");
        if (egresos == null) egresos = BigDecimal.ZERO;

        BigDecimal saldoFinal = caja.getSaldoInicial().add(ingresos).subtract(egresos);

        caja.setTotalIngresos(ingresos);
        caja.setTotalEgresos(egresos);
        caja.setSaldoFinal(saldoFinal);
        caja.setEstado(EstadoCaja.CERRADA);
        caja.setFechaCierre(LocalDateTime.now());

        CierreCaja cerrado = repository.save(caja);

        notificationService.enviarPush(
                caja.getUsuario().getFcmToken(),
                "Turno de Caja Cerrado",
                "Cierre registrado. Balance final del turno: " + formatoGs(saldoFinal)
        );

        return mapper.toResponse(cerrado);
    }

    @Override
    public void notificarArqueo(ArqueoCajaRequest request) {
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        BigDecimal dif = request.getSaldoFisico().subtract(request.getSaldoEsperado());
        String detalle;

        if (dif.compareTo(BigDecimal.ZERO) == 0) {
            detalle = "Caja cuadrada. Monto contado: " + formatoGs(request.getSaldoFisico());
        } else if (dif.compareTo(BigDecimal.ZERO) < 0) {
            detalle = "Faltante de " + formatoGs(dif.abs()) + ". Esperado: " + formatoGs(request.getSaldoEsperado()) + " | Contado: " + formatoGs(request.getSaldoFisico());
        } else {
            detalle = "Sobrante de " + formatoGs(dif) + ". Esperado: " + formatoGs(request.getSaldoEsperado()) + " | Contado: " + formatoGs(request.getSaldoFisico());
        }

        notificationService.enviarPush(
                usuario.getFcmToken(),
                "Arqueo de Caja Ejecutado",
                detalle
        );
    }

    @Override
    public CierreCajaResponse obtenerCajaAbierta(UUID empresaId, UUID sucursalId, UUID usuarioId) {
        CierreCaja caja = repository.findByEmpresaIdAndSucursalIdAndUsuarioIdAndEstado(
                empresaId,
                sucursalId,
                usuarioId,
                EstadoCaja.ABIERTA
        ).orElseThrow(() -> new ResourceNotFoundException("No hay caja abierta para los parámetros especificados"));

        return mapper.toResponse(caja);
    }

    @Override
    public List<CierreCajaResponse> listar() {
        return repository.findAllByOrderByFechaDesc().stream().map(mapper::toResponse).toList();
    }

    @Override
    public List<CierreCajaResponse> listarPorEmpresaYSucursal(UUID empresaId, UUID sucursalId, UUID usuarioId) {
        if (usuarioId != null && sucursalId != null) {
            return repository.findByEmpresaIdAndSucursalIdAndUsuarioIdOrderByFechaDesc(empresaId, sucursalId, usuarioId)
                    .stream().map(mapper::toResponse).toList();
        }
        if (sucursalId != null) {
            return repository.findByEmpresaIdAndSucursalIdOrderByFechaDesc(empresaId, sucursalId)
                    .stream().map(mapper::toResponse).toList();
        }
        return repository.findByEmpresaIdOrderByFechaDesc(empresaId)
                .stream().map(mapper::toResponse).toList();
    }

    @Override
    public CierreCajaResponse buscar(UUID id) {
        return mapper.toResponse(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cierre de caja no encontrado: " + id)));
    }
}