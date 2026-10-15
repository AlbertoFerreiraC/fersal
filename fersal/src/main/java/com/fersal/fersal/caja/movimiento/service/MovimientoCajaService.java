package com.fersal.fersal.caja.movimiento.service;

import com.fersal.fersal.caja.movimiento.dto.MovimientoCajaRequest;
import com.fersal.fersal.caja.movimiento.dto.MovimientoCajaResponse;

import java.util.List;
import java.util.UUID;

public interface MovimientoCajaService {

    MovimientoCajaResponse crear(MovimientoCajaRequest request);

    List<MovimientoCajaResponse> listar();

    List<MovimientoCajaResponse> listarPorEmpresaYSucursal(UUID empresaId, UUID sucursalId, UUID usuarioId);

    MovimientoCajaResponse buscar(UUID id);
}