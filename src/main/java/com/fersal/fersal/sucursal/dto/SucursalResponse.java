package com.fersal.fersal.sucursal.dto;


import lombok.Builder;
import lombok.Data;


import java.util.UUID;


@Data
@Builder
public class SucursalResponse {


    private UUID id;


    private UUID empresaId;


    private String empresaNombre;


    private String nombre;


    private String direccion;


    private String telefono;


    private Boolean activo;


}