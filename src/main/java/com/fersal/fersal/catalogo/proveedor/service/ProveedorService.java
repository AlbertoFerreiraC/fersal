package com.fersal.fersal.catalogo.proveedor.service;


import com.fersal.fersal.catalogo.proveedor.dto.*;

import java.util.List;
import java.util.UUID;


public interface ProveedorService {


    ProveedorResponse crear(ProveedorRequest request);


    List<ProveedorResponse> listar();


    List<ProveedorResponse> listarEmpresa(UUID empresaId);


    ProveedorResponse buscar(UUID id);


    ProveedorResponse actualizar(
            UUID id,
            ProveedorRequest request
    );


    void eliminar(UUID id);

}