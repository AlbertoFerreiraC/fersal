package com.fersal.fersal.catalogo.categoria.service;


import com.fersal.fersal.catalogo.categoria.dto.*;

import java.util.List;
import java.util.UUID;


public interface CategoriaService {


    CategoriaResponse crear(CategoriaRequest request);


    List<CategoriaResponse> listar();


    List<CategoriaResponse> listarEmpresa(UUID empresaId);


    CategoriaResponse buscar(UUID id);


    CategoriaResponse actualizar(UUID id, CategoriaRequest request);


    void eliminar(UUID id);


}