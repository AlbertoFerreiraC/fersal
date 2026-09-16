package com.fersal.fersal.inventario.stock.service;


import com.fersal.fersal.catalogo.producto.repository.ProductoRepository;
import com.fersal.fersal.empresa.repository.EmpresaRepository;
import com.fersal.fersal.inventario.movimiento.entity.MovimientoStock;
import com.fersal.fersal.inventario.movimiento.repository.MovimientoStockRepository;
import com.fersal.fersal.inventario.stock.dto.StockRequest;
import com.fersal.fersal.inventario.stock.dto.StockResponse;
import com.fersal.fersal.inventario.stock.entity.Stock;
import com.fersal.fersal.inventario.stock.mapper.StockMapper;
import com.fersal.fersal.inventario.stock.repository.StockRepository;
import com.fersal.fersal.security.repository.UsuarioRepository;
import com.fersal.fersal.sucursal.repository.SucursalRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {


    private final StockRepository repository;

    private final EmpresaRepository empresaRepository;

    private final SucursalRepository sucursalRepository;

    private final ProductoRepository productoRepository;

    private final StockMapper mapper;

    private final MovimientoStockRepository movimientoRepository;

    private final UsuarioRepository usuarioRepository;


    @Override
    public StockResponse crear(
            StockRequest request
    ) {

        if (
                repository.findBySucursalIdAndProductoId(
                        request.getSucursalId(),
                        request.getProductoId()
                ).isPresent()
        ) {

            throw new RuntimeException(
                    "El producto ya existe en esta sucursal"
            );

        }


        var empresa = empresaRepository.findById(
                request.getEmpresaId()
        ).orElseThrow(
                () -> new RuntimeException(
                        "Empresa no encontrada: "
                                + request.getEmpresaId()
                )
        );


        var sucursal = sucursalRepository.findById(
                request.getSucursalId()
        ).orElseThrow(
                () -> new RuntimeException(
                        "Sucursal no encontrada: "
                                + request.getSucursalId()
                )
        );


        var producto = productoRepository.findById(
                request.getProductoId()
        ).orElseThrow(
                () -> new RuntimeException(
                        "Producto no encontrado: "
                                + request.getProductoId()
                )
        );


        Stock stock = Stock.builder()

                .empresa(empresa)

                .sucursal(sucursal)

                .producto(producto)

                .cantidad(
                        request.getCantidad()
                )

                .stockMinimo(
                        request.getStockMinimo()
                )

                .build();


        Stock guardado = repository.save(stock);


        return mapper.toResponse(
                guardado
        );

    }


    @Override
    public List<StockResponse> listar() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();

    }


    @Override
    public List<StockResponse> listarSucursal(
            UUID sucursalId
    ) {

        return repository.findBySucursalId(sucursalId)

                .stream()

                .map(mapper::toResponse)

                .toList();

    }


    @Override
    public StockResponse buscar(UUID id) {

        return mapper.toResponse(
                repository.findById(id)
                        .orElseThrow()
        );

    }


    @Override
    public StockResponse actualizar(
            UUID id,
            StockRequest request
    ) {


        Stock stock =
                repository.findById(id)
                        .orElseThrow();


        stock.setCantidad(
                request.getCantidad()
        );


        stock.setStockMinimo(
                request.getStockMinimo()
        );


        return mapper.toResponse(
                repository.save(stock)
        );


    }

    @Override
    @Transactional
    public void descontarStock(
            UUID empresaId,
            UUID sucursalId,
            UUID productoId,
            UUID usuarioId,
            Integer cantidad,
            String observacion
    ) {


        Stock stock =
                repository.findBySucursalIdAndProductoId(
                                sucursalId,
                                productoId
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "No existe stock para el producto"
                                )
                        );


        if (stock.getCantidad() < cantidad) {

            throw new RuntimeException(
                    "Stock insuficiente"
            );

        }


        Integer stockAnterior =
                stock.getCantidad();


        Integer stockNuevo =
                stockAnterior - cantidad;


        stock.setCantidad(stockNuevo);


        repository.save(stock);


        MovimientoStock movimiento =
                MovimientoStock.builder()

                        .empresa(stock.getEmpresa())

                        .sucursal(stock.getSucursal())

                        .producto(stock.getProducto())

                        .usuario(
                                usuarioRepository
                                        .findById(usuarioId)
                                        .orElseThrow()
                        )

                        .tipoMovimiento(
                                "SALIDA"
                        )

                        .cantidad(cantidad)

                        .stockAnterior(stockAnterior)

                        .stockNuevo(stockNuevo)

                        .observacion(observacion)

                        .build();


        movimientoRepository.save(movimiento);

    }

    @Override
    @Transactional
    public void aumentarStock(
            UUID empresaId,
            UUID sucursalId,
            UUID productoId,
            UUID usuarioId,
            Integer cantidad,
            String observacion
    ) {


        Stock stock =
                repository.findBySucursalIdAndProductoId(
                                sucursalId,
                                productoId
                        )
                        .orElseThrow();


        Integer stockAnterior =
                stock.getCantidad();


        Integer stockNuevo =
                stockAnterior + cantidad;


        stock.setCantidad(stockNuevo);


        repository.save(stock);


        MovimientoStock movimiento =
                MovimientoStock.builder()

                        .empresa(stock.getEmpresa())

                        .sucursal(stock.getSucursal())

                        .producto(stock.getProducto())

                        .usuario(
                                usuarioRepository
                                        .findById(usuarioId)
                                        .orElseThrow()
                        )

                        .tipoMovimiento(
                                "ENTRADA"
                        )

                        .cantidad(cantidad)

                        .stockAnterior(stockAnterior)

                        .stockNuevo(stockNuevo)

                        .observacion(observacion)

                        .build();


        movimientoRepository.save(movimiento);

    }

    @Override
    public List<StockResponse> bajoStock() {


        return repository
                .findAll()
                .stream()

                .filter(
                        s -> s.getCantidad()
                                <= s.getStockMinimo()
                )

                .map(mapper::toResponse)

                .toList();

    }

}