package com.fersal.fersal.venta.service;

import com.fersal.fersal.caja.cierre.entity.EstadoCaja;
import com.fersal.fersal.caja.cierre.repository.CierreCajaRepository;
import com.fersal.fersal.caja.movimiento.dto.MovimientoCajaRequest;
import com.fersal.fersal.caja.movimiento.service.MovimientoCajaService;
import com.fersal.fersal.catalogo.producto.entity.Producto;
import com.fersal.fersal.catalogo.producto.repository.ProductoRepository;
import com.fersal.fersal.cliente.cuenta.entity.CuentaCorriente;
import com.fersal.fersal.cliente.cuenta.repository.CuentaCorrienteRepository;
import com.fersal.fersal.cliente.repository.ClienteRepository;
import com.fersal.fersal.empresa.repository.EmpresaRepository;
import com.fersal.fersal.exception.BusinessException;
import com.fersal.fersal.exception.ResourceNotFoundException;
import com.fersal.fersal.inventario.stock.service.StockService;
import com.fersal.fersal.security.repository.UsuarioRepository;
import com.fersal.fersal.sucursal.repository.SucursalRepository;
import com.fersal.fersal.venta.dto.DetalleVentaRequest;
import com.fersal.fersal.venta.dto.VentaRequest;
import com.fersal.fersal.venta.dto.VentaResponse;
import com.fersal.fersal.venta.entity.DetalleVenta;
import com.fersal.fersal.venta.entity.Venta;
import com.fersal.fersal.venta.mapper.VentaMapper;
import com.fersal.fersal.venta.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VentaServiceImpl implements VentaService {

    private final VentaRepository repository;
    private final EmpresaRepository empresaRepository;
    private final SucursalRepository sucursalRepository;
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final CuentaCorrienteRepository cuentaRepository;
    private final ProductoRepository productoRepository;
    private final VentaMapper mapper;
    private final StockService stockService;
    private final MovimientoCajaService movimientoCajaService;
    private final CierreCajaRepository cierreCajaRepository;

    @Override
    @Transactional
    public VentaResponse crear(VentaRequest request) {

        boolean tieneCajaAbierta = cierreCajaRepository.existsByEmpresaIdAndSucursalIdAndUsuarioIdAndEstado(
                request.getEmpresaId(),
                request.getSucursalId(),
                request.getUsuarioId(),
                EstadoCaja.ABIERTA
        );

        if (!tieneCajaAbierta) {
            throw new BusinessException("No puede registrar ventas: La caja de este usuario en esta sucursal no se encuentra abierta");
        }

        Venta venta = Venta.builder()
                .empresa(empresaRepository.findById(request.getEmpresaId())
                        .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada")))
                .sucursal(sucursalRepository.findById(request.getSucursalId())
                        .orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada")))
                .usuario(usuarioRepository.findById(request.getUsuarioId())
                        .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado")))
                .cliente(request.getClienteId() != null
                        ? clienteRepository.findById(request.getClienteId())
                        .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"))
                        : null)
                .numero(generarNumero(request.getEmpresaId()))
                .tipoPago(request.getTipoPago())
                .observacion(request.getObservacion())
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (DetalleVentaRequest item : request.getDetalles()) {
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado: " + item.getProductoId()));

            BigDecimal subtotal = producto.getPrecioVenta().multiply(BigDecimal.valueOf(item.getCantidad()));

            stockService.descontarStock(
                    request.getEmpresaId(),
                    request.getSucursalId(),
                    item.getProductoId(),
                    request.getUsuarioId(),
                    item.getCantidad(),
                    "Venta " + venta.getNumero()
            );

            DetalleVenta detalle = DetalleVenta.builder()
                    .venta(venta)
                    .producto(producto)
                    .cantidad(item.getCantidad())
                    .precioUnitario(producto.getPrecioVenta())
                    .subtotal(subtotal)
                    .build();

            venta.getDetalles().add(detalle);
            total = total.add(subtotal);
        }

        venta.setSubtotal(total);
        venta.setTotal(total);

        if (esVentaCredito(request.getTipoPago())) {
            if (request.getClienteId() == null) {
                throw new BusinessException("Una venta a crédito necesita un cliente asignado");
            }

            CuentaCorriente cuenta = cuentaRepository.findByClienteId(request.getClienteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cuenta corriente no encontrada para el cliente"));

            BigDecimal nuevoSaldo = cuenta.getSaldo().add(total);

            if (nuevoSaldo.compareTo(cuenta.getLimiteCredito()) > 0) {
                throw new BusinessException("La venta supera el límite de crédito disponible (" + cuenta.getLimiteCredito() + ")");
            }

            cuenta.setSaldo(nuevoSaldo);
            cuentaRepository.save(cuenta);

            venta.setEstado("PENDIENTE DE PAGO");
            venta.setFechaVencimiento(LocalDateTime.now().plusDays(30));
        } else {
            venta.setEstado("PAGADA");
            registrarMovimientoCaja(request, venta, total);
        }

        return mapper.toResponse(repository.save(venta));
    }

    private boolean esVentaCredito(String tipoPago) {
        if (tipoPago == null) return false;
        return tipoPago.equalsIgnoreCase("CUENTA_CORRIENTE")
                || tipoPago.equalsIgnoreCase("CREDITO")
                || tipoPago.equalsIgnoreCase("FIADO");
    }

    private String generarNumero(UUID empresaId) {
        long numero = repository.countByEmpresaId(empresaId) + 1;
        return String.format("V-%06d", numero);
    }

    private void registrarMovimientoCaja(VentaRequest request, Venta venta, BigDecimal total) {
        MovimientoCajaRequest movimiento = MovimientoCajaRequest.builder()
                .empresaId(request.getEmpresaId())
                .sucursalId(request.getSucursalId())
                .usuarioId(request.getUsuarioId())
                .tipo("INGRESO")
                .monto(total)
                .descripcion("Venta nro " + venta.getNumero())
                .build();

        movimientoCajaService.crear(movimiento);
    }

    @Override
    public List<VentaResponse> listar() {
        return repository.findAllByOrderByFechaDesc()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public List<VentaResponse> listarPorEmpresaYSucursal(UUID empresaId, UUID sucursalId) {
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
    public VentaResponse buscar(UUID id) {
        return mapper.toResponse(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada")));
    }

    @Override
    @Transactional
    public VentaResponse anular(UUID id, UUID usuarioId, String motivo) {
        Venta venta = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada"));

        if ("ANULADA".equalsIgnoreCase(venta.getEstado())) {
            throw new BusinessException("La venta ya se encuentra anulada");
        }

        for (DetalleVenta detalle : venta.getDetalles()) {
            stockService.aumentarStock(
                    venta.getEmpresa().getId(),
                    venta.getSucursal().getId(),
                    detalle.getProducto().getId(),
                    usuarioId,
                    detalle.getCantidad(),
                    "Anulación venta " + venta.getNumero()
            );
        }

        if ("PAGADA".equalsIgnoreCase(venta.getEstado())) {
            MovimientoCajaRequest movimiento = MovimientoCajaRequest.builder()
                    .empresaId(venta.getEmpresa().getId())
                    .sucursalId(venta.getSucursal().getId())
                    .usuarioId(usuarioId)
                    .tipo("SALIDA")
                    .monto(venta.getTotal())
                    .descripcion("Anulación " + venta.getNumero())
                    .build();

            movimientoCajaService.crear(movimiento);
        }

        if ("PENDIENTE DE PAGO".equalsIgnoreCase(venta.getEstado()) && venta.getCliente() != null) {
            CuentaCorriente cuenta = cuentaRepository.findByClienteId(venta.getCliente().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cuenta corriente no encontrada para el cliente"));

            cuenta.setSaldo(cuenta.getSaldo().subtract(venta.getTotal()));
            cuentaRepository.save(cuenta);
        }

        venta.setEstado("ANULADA");
        venta.setMotivoAnulacion(motivo);
        venta.setFechaAnulacion(LocalDateTime.now());
        venta.setUsuarioAnulacion(usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario de anulación no encontrado")));

        return mapper.toResponse(repository.save(venta));
    }
}