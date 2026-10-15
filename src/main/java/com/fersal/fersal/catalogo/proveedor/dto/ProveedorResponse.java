package com.fersal.fersal.catalogo.proveedor.dto;


import lombok.Builder;
import lombok.Data;


import java.util.UUID;


@Data
@Builder
public class ProveedorResponse {


    private UUID id;


    private UUID empresaId;


    private String empresaNombre;


    private String nombre;


    private String ruc;


    private String telefono;


    private String email;


    private String direccion;


    private Boolean activo;


}