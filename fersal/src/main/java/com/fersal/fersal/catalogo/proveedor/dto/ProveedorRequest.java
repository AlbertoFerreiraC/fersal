package com.fersal.fersal.catalogo.proveedor.dto;


import lombok.Data;

import java.util.UUID;


@Data
public class ProveedorRequest {


    private UUID empresaId;


    private String nombre;


    private String ruc;


    private String telefono;


    private String email;


    private String direccion;


}