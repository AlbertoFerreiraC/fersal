package com.fersal.fersal.sucursal.dto;


import jakarta.validation.constraints.NotBlank;

import lombok.Data;


import java.util.UUID;


@Data
public class SucursalRequest {


    @NotBlank
    private String nombre;


    private String direccion;


    private String telefono;


    private UUID empresaId;


}