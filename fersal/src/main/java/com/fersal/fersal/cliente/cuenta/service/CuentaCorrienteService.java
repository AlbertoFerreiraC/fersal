package com.fersal.fersal.cliente.cuenta.service;

import com.fersal.fersal.cliente.cuenta.dto.CuentaCorrienteRequest;
import com.fersal.fersal.cliente.cuenta.dto.CuentaCorrienteResponse;

import java.util.List;
import java.util.UUID;

public interface CuentaCorrienteService {

    CuentaCorrienteResponse crear(CuentaCorrienteRequest request);

    List<CuentaCorrienteResponse> listar();

    List<CuentaCorrienteResponse> listarEmpresa(UUID empresaId);

    CuentaCorrienteResponse buscar(UUID id);

    CuentaCorrienteResponse buscarCliente(UUID clienteId);

    CuentaCorrienteResponse actualizar(UUID id, CuentaCorrienteRequest request);

    void eliminar(UUID id);
}