package com.fersal.fersal.catalogo.producto.service;

import com.fersal.fersal.catalogo.producto.dto.ProductoRequest;
import com.fersal.fersal.catalogo.producto.dto.ProductoResponse;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ProductoService {

    ProductoResponse crear(
            ProductoRequest request
    );

    List<ProductoResponse> listar();

    List<ProductoResponse> listarEmpresa(
            UUID empresaId
    );

    ProductoResponse buscar(
            UUID id
    );

    ProductoResponse actualizar(
            UUID id,
            ProductoRequest request
    );

    ProductoResponse subirImagen(
            UUID productoId,
            MultipartFile imagen
    );

    void eliminarImagen(
            UUID productoId
    );

    void eliminar(
            UUID id
    );
}