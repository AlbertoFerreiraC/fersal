package com.fersal.fersal.catalogo.categoria.dto;


import lombok.Builder;
import lombok.Data;

import java.util.UUID;


@Data
@Builder
public class CategoriaResponse {


    private UUID id;

    private UUID empresaId;

    private String empresaNombre;

    private String nombre;

    private String descripcion;

    private Boolean activo;


}