package com.fersal.fersal.venta.service;


import com.fersal.fersal.caja.movimiento.dto.MovimientoCajaRequest;
import com.fersal.fersal.caja.movimiento.service.MovimientoCajaService;
import com.fersal.fersal.inventario.stock.service.StockService;
import com.fersal.fersal.venta.dto.*;
import com.fersal.fersal.venta.entity.*;
import com.fersal.fersal.venta.mapper.VentaMapper;
import com.fersal.fersal.venta.repository.VentaRepository;


import com.fersal.fersal.catalogo.producto.entity.Producto;
import com.fersal.fersal.catalogo.producto.repository.ProductoRepository;


import com.fersal.fersal.empresa.repository.EmpresaRepository;
import com.fersal.fersal.sucursal.repository.SucursalRepository;

import com.fersal.fersal.security.repository.UsuarioRepository;

import com.fersal.fersal.cliente.repository.ClienteRepository;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


import org.springframework.stereotype.Service;


import java.math.BigDecimal;
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

    private final ProductoRepository productoRepository;

    private final VentaMapper mapper;

    private final StockService stockService;

    private final MovimientoCajaService movimientoCajaService;

    @Override
    @Transactional
    public VentaResponse crear(VentaRequest request) {


        Venta venta = Venta.builder()

                .empresa(empresaRepository.findById(request.getEmpresaId()).orElseThrow())

                .sucursal(sucursalRepository.findById(request.getSucursalId()).orElseThrow())

                .usuario(usuarioRepository.findById(request.getUsuarioId()).orElseThrow())

                .cliente(request.getClienteId() != null ? clienteRepository.findById(request.getClienteId()).orElse(null) : null)

                .numero(generarNumero(request.getEmpresaId()))

                .tipoPago(request.getTipoPago())

                .observacion(request.getObservacion())

                .build();


        BigDecimal total = BigDecimal.ZERO;


        for (DetalleVentaRequest item : request.getDetalles()) {


            Producto producto =
                    productoRepository
                            .findById(item.getProductoId())
                            .orElseThrow();


            BigDecimal subtotal =
                    producto.getPrecioVenta()
                            .multiply(
                                    BigDecimal.valueOf(
                                            item.getCantidad()
                                    )
                            );


            stockService.descontarStock(
                    request.getEmpresaId(),
                    request.getSucursalId(),
                    item.getProductoId(),
                    request.getUsuarioId(),
                    item.getCantidad(),
                    "Venta " + venta.getNumero()
            );


            DetalleVenta detalle =
                    DetalleVenta.builder()

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

        registrarMovimientoCaja(
                request,
                venta,
                total
        );

        return mapper.toResponse(repository.save(venta));

    }


    private String generarNumero(UUID empresaId) {

        long numero = repository.countByEmpresaId(empresaId) + 1;


        return String.format("V-%06d", numero);

    }

    private void registrarMovimientoCaja(
            VentaRequest request,
            Venta venta,
            BigDecimal total
    ) {

        MovimientoCajaRequest movimiento =
                MovimientoCajaRequest.builder()

                        .empresaId(
                                request.getEmpresaId()
                        )

                        .sucursalId(
                                request.getSucursalId()
                        )

                        .usuarioId(
                                request.getUsuarioId()
                        )

                        .tipo(
                                "INGRESO"
                        )

                        .monto(
                                total
                        )

                        .descripcion(
                                "Venta nro " + venta.getNumero()
                        )

                        .build();

        movimientoCajaService.crear(
                movimiento
        );

    }

    @Override
    public List<VentaResponse> listar() {


        return repository.findAll().stream().map(mapper::toResponse).toList();

    }


    @Override
    public VentaResponse buscar(UUID id) {


        return mapper.toResponse(repository.findById(id).orElseThrow());

    }


}