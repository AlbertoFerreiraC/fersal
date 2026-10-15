package com.fersal.fersal.venta.service;

import com.fersal.fersal.venta.dto.VentaRequest;
import com.fersal.fersal.venta.dto.VentaResponse;

import java.util.List;
import java.util.UUID;

public interface VentaService {

    VentaResponse crear(VentaRequest request);

    List<VentaResponse> listar();

    List<VentaResponse> listarPorEmpresaYSucursal(UUID empresaId, UUID sucursalId);

    VentaResponse buscar(UUID id);

    VentaResponse anular(UUID id, UUID usuarioId, String motivo);
}