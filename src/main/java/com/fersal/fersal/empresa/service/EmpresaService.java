package com.fersal.fersal.empresa.service;


import com.fersal.fersal.empresa.dto.EmpresaRequest;
import com.fersal.fersal.empresa.dto.EmpresaResponse;

import java.util.List;
import java.util.UUID;


public interface EmpresaService {


    List<EmpresaResponse> listar();


    EmpresaResponse buscar(UUID id);


    EmpresaResponse crear(EmpresaRequest request);


    EmpresaResponse actualizar(
            UUID id,
            EmpresaRequest request
    );


    void eliminar(UUID id);


}