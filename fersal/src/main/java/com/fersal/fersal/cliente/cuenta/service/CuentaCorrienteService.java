package com.fersal.fersal.cliente.cuenta.service;


import com.fersal.fersal.cliente.cuenta.dto.*;

import java.util.List;
import java.util.UUID;


public interface CuentaCorrienteService {


    CuentaCorrienteResponse crear(
            CuentaCorrienteRequest request
    );


    List<CuentaCorrienteResponse> listar();


    CuentaCorrienteResponse buscar(
            UUID id
    );


    CuentaCorrienteResponse buscarCliente(
            UUID clienteId
    );


    CuentaCorrienteResponse actualizar(
            UUID id,
            CuentaCorrienteRequest request
    );


    void eliminar(
            UUID id
    );

}