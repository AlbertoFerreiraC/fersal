package com.fersal.fersal.caja.movimiento.service;

import com.fersal.fersal.caja.cierre.entity.EstadoCaja;
import com.fersal.fersal.caja.cierre.repository.CierreCajaRepository;
import com.fersal.fersal.caja.movimiento.dto.MovimientoCajaRequest;
import com.fersal.fersal.caja.movimiento.dto.MovimientoCajaResponse;
import com.fersal.fersal.caja.movimiento.entity.MovimientoCaja;
import com.fersal.fersal.caja.movimiento.mapper.MovimientoCajaMapper;
import com.fersal.fersal.caja.movimiento.repository.MovimientoCajaRepository;
import com.fersal.fersal.empresa.repository.EmpresaRepository;
import com.fersal.fersal.exception.BusinessException;
import com.fersal.fersal.exception.ResourceNotFoundException;
import com.fersal.fersal.notification.service.NotificationService;
import com.fersal.fersal.security.entity.Usuario;
import com.fersal.fersal.security.repository.UsuarioRepository;
import com.fersal.fersal.sucursal.repository.SucursalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MovimientoCajaServiceImpl implements MovimientoCajaService {

    private final MovimientoCajaRepository repository;
    private final EmpresaRepository empresaRepository;
    private final SucursalRepository sucursalRepository;
    private final UsuarioRepository usuarioRepository;
    private final CierreCajaRepository cierreCajaRepository;
    private final MovimientoCajaMapper mapper;
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
    public MovimientoCajaResponse crear(MovimientoCajaRequest request) {
        boolean esApertura = request.getDescripcion() != null &&
                request.getDescripcion().toUpperCase().contains("APERTURA");

        if (!esApertura) {
            boolean tieneCajaAbierta = cierreCajaRepository.existsByEmpresaIdAndSucursalIdAndUsuarioIdAndEstado(
                    request.getEmpresaId(),
                    request.getSucursalId(),
                    request.getUsuarioId(),
                    EstadoCaja.ABIERTA
            );

            if (!tieneCajaAbierta) {
                throw new BusinessException("No puede registrar movimientos: La caja de este usuario no está abierta");
            }
        }

        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        MovimientoCaja movimiento = MovimientoCaja.builder()
                .empresa(empresaRepository.findById(request.getEmpresaId()).orElseThrow())
                .sucursal(sucursalRepository.findById(request.getSucursalId()).orElseThrow())
                .usuario(usuario)
                .tipo(request.getTipo())
                .monto(request.getMonto())
                .descripcion(request.getDescripcion())
                .build();

        MovimientoCaja guardado = repository.save(movimiento);

        if (!esApertura) {
            String signo = "INGRESO".equalsIgnoreCase(request.getTipo()) ? "(+)" : "(-)";
            notificationService.enviarPush(
                    usuario.getFcmToken(),
                    "Movimiento en Caja " + signo,
                    request.getDescripcion() + " por " + formatoGs(request.getMonto())
            );
        }

        return mapper.toResponse(guardado);
    }

    @Override
    public List<MovimientoCajaResponse> listar() {
        return repository.findAllByOrderByFechaDesc()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public List<MovimientoCajaResponse> listarPorEmpresaYSucursal(UUID empresaId, UUID sucursalId, UUID usuarioId) {
        if (usuarioId != null && sucursalId != null) {
            return repository.findByEmpresaIdAndSucursalIdAndUsuarioIdOrderByFechaDesc(empresaId, sucursalId, usuarioId)
                    .stream()
                    .map(mapper::toResponse)
                    .toList();
        }
        if (sucursalId != null) {
            return repository.findByEmpresaIdAndSucursalIdOrderByFechaDesc(empresaId, sucursalId)
                    .stream()
                    .map(mapper::toResponse)
                    .toList();
        }
        return repository.findByEmpresaIdOrderByFechaDesc(empresaId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public MovimientoCajaResponse buscar(UUID id) {
        return mapper.toResponse(repository.findById(id).orElseThrow());
    }
}