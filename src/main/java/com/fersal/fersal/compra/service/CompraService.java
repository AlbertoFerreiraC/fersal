package com.fersal.fersal.compra.service;

import com.fersal.fersal.compra.dto.CompraRequest;
import com.fersal.fersal.compra.dto.CompraResponse;

import java.util.List;
import java.util.UUID;

public interface CompraService {
    CompraResponse registrarCompra(CompraRequest request);

    List<CompraResponse> listarPorEmpresaYSucursal(UUID empresaId, UUID sucursalId);

    CompraResponse buscar(UUID id);
}