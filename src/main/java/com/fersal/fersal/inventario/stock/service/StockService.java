package com.fersal.fersal.inventario.stock.service;

import com.fersal.fersal.inventario.stock.dto.StockRequest;
import com.fersal.fersal.inventario.stock.dto.StockResponse;

import java.util.List;
import java.util.UUID;

public interface StockService {

    StockResponse crear(StockRequest request);

    List<StockResponse> listar();

    List<StockResponse> listarPorEmpresaYSucursal(UUID empresaId, UUID sucursalId);

    List<StockResponse> listarSucursal(UUID sucursalId);

    List<StockResponse> bajoStock();

    List<StockResponse> bajoStockPorEmpresa(UUID empresaId);

    StockResponse buscar(UUID id);

    StockResponse actualizar(UUID id, StockRequest request);

    void descontarStock(
            UUID empresaId,
            UUID sucursalId,
            UUID productoId,
            UUID usuarioId,
            Integer cantidad,
            String observacion
    );

    void aumentarStock(
            UUID empresaId,
            UUID sucursalId,
            UUID productoId,
            UUID usuarioId,
            Integer cantidad,
            String observacion
    );
}