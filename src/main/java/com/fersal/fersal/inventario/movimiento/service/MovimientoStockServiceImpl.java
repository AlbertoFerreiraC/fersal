package com.fersal.fersal.inventario.movimiento.service;

import com.fersal.fersal.catalogo.producto.repository.ProductoRepository;
import com.fersal.fersal.empresa.repository.EmpresaRepository;
import com.fersal.fersal.inventario.movimiento.dto.MovimientoRequest;
import com.fersal.fersal.inventario.movimiento.dto.MovimientoResponse;
import com.fersal.fersal.inventario.movimiento.entity.MovimientoStock;
import com.fersal.fersal.inventario.movimiento.mapper.MovimientoStockMapper;
import com.fersal.fersal.inventario.movimiento.repository.MovimientoStockRepository;
import com.fersal.fersal.inventario.stock.entity.Stock;
import com.fersal.fersal.inventario.stock.repository.StockRepository;
import com.fersal.fersal.security.repository.UsuarioRepository;
import com.fersal.fersal.sucursal.repository.SucursalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MovimientoStockServiceImpl implements MovimientoStockService {

    private final MovimientoStockRepository repository;
    private final StockRepository stockRepository;
    private final EmpresaRepository empresaRepository;
    private final SucursalRepository sucursalRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final MovimientoStockMapper mapper;

    @Override
    @Transactional
    public MovimientoResponse crear(MovimientoRequest request) {
        Stock stock = stockRepository
                .findBySucursalIdAndProductoId(
                        request.getSucursalId(),
                        request.getProductoId()
                )
                .orElseThrow(
                        () -> new RuntimeException("No existe stock para este producto en la sucursal indicada")
                );

        Integer anterior = stock.getCantidad();
        Integer nuevo;

        if (request.getTipoMovimiento().equalsIgnoreCase("ENTRADA")) {
            nuevo = anterior + request.getCantidad();
        } else if (request.getTipoMovimiento().equalsIgnoreCase("SALIDA")) {
            nuevo = anterior - request.getCantidad();
            if (nuevo < 0) {
                throw new RuntimeException("Stock insuficiente para realizar la salida");
            }
        } else {
            throw new RuntimeException("Tipo de movimiento inválido: " + request.getTipoMovimiento());
        }

        stock.setCantidad(nuevo);
        stockRepository.save(stock);

        MovimientoStock movimiento = MovimientoStock.builder()
                .empresa(empresaRepository.findById(request.getEmpresaId()).orElseThrow())
                .sucursal(sucursalRepository.findById(request.getSucursalId()).orElseThrow())
                .producto(productoRepository.findById(request.getProductoId()).orElseThrow())
                .usuario(usuarioRepository.findById(request.getUsuarioId()).orElseThrow())
                .tipoMovimiento(request.getTipoMovimiento().toUpperCase())
                .cantidad(request.getCantidad())
                .stockAnterior(anterior)
                .stockNuevo(nuevo)
                .observacion(request.getObservacion())
                .build();

        return mapper.toResponse(repository.save(movimiento));
    }

    @Override
    public List<MovimientoResponse> listar() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public List<MovimientoResponse> listarPorEmpresaYSucursal(UUID empresaId, UUID sucursalId) {
        if (sucursalId != null) {
            return repository.findByEmpresaIdAndSucursalId(empresaId, sucursalId)
                    .stream()
                    .map(mapper::toResponse)
                    .toList();
        }
        return repository.findByEmpresaId(empresaId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public List<MovimientoResponse> listarProducto(UUID productoId) {
        return repository.findByProductoId(productoId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}