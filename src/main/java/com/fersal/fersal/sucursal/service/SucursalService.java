package com.fersal.fersal.sucursal.service;


import com.fersal.fersal.sucursal.dto.SucursalRequest;
import com.fersal.fersal.sucursal.dto.SucursalResponse;


import java.util.List;
import java.util.UUID;


public interface SucursalService {


    List<SucursalResponse> listar();


    List<SucursalResponse> listarPorEmpresa(UUID empresaId);


    SucursalResponse buscar(UUID id);


    SucursalResponse crear(SucursalRequest request);


    SucursalResponse actualizar(
            UUID id,
            SucursalRequest request
    );


    void eliminar(UUID id);


}