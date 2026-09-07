package com.fersal.fersal.catalogo.categoria.dto;


import lombok.Data;

import java.util.UUID;


@Data
public class CategoriaRequest {


    private UUID empresaId;


    private String nombre;


    private String descripcion;


}