package com.fersal.fersal.cliente.service;


import com.fersal.fersal.cliente.dto.*;

import java.util.List;
import java.util.UUID;


public interface ClienteService {


    ClienteResponse crear(
            ClienteRequest request
    );


    List<ClienteResponse> listar();


    List<ClienteResponse> listarEmpresa(
            UUID empresaId
    );


    ClienteResponse buscar(
            UUID id
    );


    ClienteResponse actualizar(
            UUID id,
            ClienteRequest request
    );


    void eliminar(
            UUID id
    );


}