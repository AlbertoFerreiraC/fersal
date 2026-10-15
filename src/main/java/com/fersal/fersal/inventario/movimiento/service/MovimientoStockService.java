package com.fersal.fersal.inventario.movimiento.service;

import com.fersal.fersal.inventario.movimiento.dto.MovimientoRequest;
import com.fersal.fersal.inventario.movimiento.dto.MovimientoResponse;

import java.util.List;
import java.util.UUID;

public interface MovimientoStockService {

    MovimientoResponse crear(MovimientoRequest request);

    List<MovimientoResponse> listar();

    List<MovimientoResponse> listarPorEmpresaYSucursal(UUID empresaId, UUID sucursalId);

    List<MovimientoResponse> listarProducto(UUID productoId);
}