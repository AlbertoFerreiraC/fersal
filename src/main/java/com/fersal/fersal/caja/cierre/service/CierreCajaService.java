package com.fersal.fersal.caja.cierre.service;

import com.fersal.fersal.caja.cierre.dto.AperturaCajaRequest;
import com.fersal.fersal.caja.cierre.dto.ArqueoCajaRequest;
import com.fersal.fersal.caja.cierre.dto.CierreCajaRequest;
import com.fersal.fersal.caja.cierre.dto.CierreCajaResponse;

import java.util.List;
import java.util.UUID;

public interface CierreCajaService {
    CierreCajaResponse abrir(AperturaCajaRequest request);

    CierreCajaResponse cerrar(CierreCajaRequest request);

    void notificarArqueo(ArqueoCajaRequest request);

    CierreCajaResponse obtenerCajaAbierta(UUID empresaId, UUID sucursalId, UUID usuarioId);

    List<CierreCajaResponse> listar();

    List<CierreCajaResponse> listarPorEmpresaYSucursal(UUID empresaId, UUID sucursalId, UUID usuarioId);

    CierreCajaResponse buscar(UUID id);
}