package com.fersal.fersal.cliente.pago.service;


import com.fersal.fersal.cliente.pago.dto.*;


import java.util.List;
import java.util.UUID;


public interface PagoClienteService {


    PagoClienteResponse crear(
            PagoClienteRequest request
    );


    List<PagoClienteResponse> listar();


    List<PagoClienteResponse> listarCliente(
            UUID clienteId
    );


    List<PagoClienteResponse> listarCuenta(
            UUID cuentaId
    );


    PagoClienteResponse buscar(
            UUID id
    );


}