package com.fersal.fersal.compra.service;

import com.fersal.fersal.caja.movimiento.dto.MovimientoCajaRequest;
import com.fersal.fersal.caja.movimiento.service.MovimientoCajaService;
import com.fersal.fersal.catalogo.producto.entity.Producto;
import com.fersal.fersal.catalogo.producto.repository.ProductoRepository;
import com.fersal.fersal.catalogo.proveedor.entity.Proveedor;
import com.fersal.fersal.catalogo.proveedor.repository.ProveedorRepository;
import com.fersal.fersal.compra.cuenta.entity.CuentaPorPagar;
import com.fersal.fersal.compra.compra.repository.CuentaPorPagarRepository;
import com.fersal.fersal.compra.dto.CompraRequest;
import com.fersal.fersal.compra.dto.CompraResponse;
import com.fersal.fersal.compra.entity.Compra;
import com.fersal.fersal.compra.entity.DetalleCompra;
import com.fersal.fersal.compra.repository.CompraRepository;
import com.fersal.fersal.empresa.entity.Empresa;
import com.fersal.fersal.empresa.repository.EmpresaRepository;
import com.fersal.fersal.exception.ResourceNotFoundException;
import com.fersal.fersal.inventario.stock.service.StockService;
import com.fersal.fersal.security.entity.Usuario;
import com.fersal.fersal.security.repository.UsuarioRepository;
import com.fersal.fersal.sucursal.entity.Sucursal;
import com.fersal.fersal.sucursal.repository.SucursalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompraServiceImpl implements CompraService {

    private final CompraRepository compraRepository;
    private final EmpresaRepository empresaRepository;
    private final SucursalRepository sucursalRepository;
    private final ProveedorRepository proveedorRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final StockService stockService;
    private final MovimientoCajaService movimientoCajaService;
    private final CuentaPorPagarRepository cuentaPorPagarRepository;

    @Override
    @Transactional
    public CompraResponse registrarCompra(CompraRequest request) {
        Empresa empresa = empresaRepository.findById(request.getEmpresaId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada"));
        Sucursal sucursal = sucursalRepository.findById(request.getSucursalId())
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada"));
        Proveedor proveedor = proveedorRepository.findById(request.getProveedorId())
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado"));
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Compra compra = Compra.builder()
                .empresa(empresa)
                .sucursal(sucursal)
                .proveedor(proveedor)
                .usuario(usuario)
                .numeroFactura(request.getNumeroFactura())
                .condicionPago(request.getCondicionPago().toUpperCase())
                .observacion(request.getObservacion())
                .fecha(LocalDateTime.now())
                .estado("COMPLETADA")
                .detalles(new ArrayList<>())
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (CompraRequest.DetalleCompraItem item : request.getDetalles()) {
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado: " + item.getProductoId()));

            BigDecimal subtotal = item.getPrecioUnitario().multiply(BigDecimal.valueOf(item.getCantidad()));

            // 1. Aumentar stock atómico
            stockService.aumentarStock(
                    empresa.getId(),
                    sucursal.getId(),
                    producto.getId(),
                    usuario.getId(),
                    item.getCantidad(),
                    "Compra Fac. " + (request.getNumeroFactura() != null ? request.getNumeroFactura() : "S/N")
            );

            // 2. Actualizar último precio de compra en catálogo
            producto.setPrecioCompra(item.getPrecioUnitario());
            productoRepository.save(producto);

            DetalleCompra detalle = DetalleCompra.builder()
                    .compra(compra)
                    .producto(producto)
                    .cantidad(item.getCantidad())
                    .precioUnitario(item.getPrecioUnitario())
                    .subtotal(subtotal)
                    .build();

            compra.getDetalles().add(detalle);
            total = total.add(subtotal);
        }

        compra.setTotal(total);
        Compra compraGuardada = compraRepository.save(compra);

        // 3. Impacto contable / financiero
        if ("CONTADO".equalsIgnoreCase(request.getCondicionPago()) && request.isPagarDesdeCaja()) {
            movimientoCajaService.crear(MovimientoCajaRequest.builder()
                    .empresaId(empresa.getId())
                    .sucursalId(sucursal.getId())
                    .usuarioId(usuario.getId())
                    .tipo("SALIDA")
                    .monto(total)
                    .descripcion("Pago Compra Fac. " + (request.getNumeroFactura() != null ? request.getNumeroFactura() : ""))
                    .build());
        } else if ("CREDITO".equalsIgnoreCase(request.getCondicionPago())) {
            cuentaPorPagarRepository.save(CuentaPorPagar.builder()
                    .empresa(empresa)
                    .proveedor(proveedor)
                    .compra(compraGuardada)
                    .montoTotal(total)
                    .saldoPendiente(total)
                    .fechaEmision(LocalDateTime.now())
                    .fechaVencimiento(LocalDateTime.now().plusDays(30))
                    .estado("PENDIENTE")
                    .activo(true)
                    .build());
        }

        return mapToResponse(compraGuardada);
    }

    @Override
    public List<CompraResponse> listarPorEmpresaYSucursal(UUID empresaId, UUID sucursalId) {
        List<Compra> compras = (sucursalId != null)
                ? compraRepository.findByEmpresaIdAndSucursalIdOrderByFechaDesc(empresaId, sucursalId)
                : compraRepository.findByEmpresaIdOrderByFechaDesc(empresaId);

        return compras.stream().map(this::mapToResponse).toList();
    }

    @Override
    public CompraResponse buscar(UUID id) {
        return mapToResponse(compraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compra no encontrada: " + id)));
    }

    private CompraResponse mapToResponse(Compra compra) {
        return CompraResponse.builder()
                .id(compra.getId())
                .numeroFactura(compra.getNumeroFactura())
                .proveedor(compra.getProveedor().getNombre())
                .sucursal(compra.getSucursal().getNombre())
                .condicionPago(compra.getCondicionPago())
                .total(compra.getTotal())
                .estado(compra.getEstado())
                .fecha(compra.getFecha())
                .detalles(compra.getDetalles().stream().map(d -> CompraResponse.DetalleCompraResponse.builder()
                        .productoId(d.getProducto().getId())
                        .producto(d.getProducto().getNombre())
                        .cantidad(d.getCantidad())
                        .precioUnitario(d.getPrecioUnitario())
                        .subtotal(d.getSubtotal())
                        .build()).toList())
                .build();
    }
}