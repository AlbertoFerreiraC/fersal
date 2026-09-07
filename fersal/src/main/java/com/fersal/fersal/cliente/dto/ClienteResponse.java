package com.fersal.fersal.cliente.dto;


import lombok.Builder;
import lombok.Data;

import java.util.UUID;


@Data
@Builder
public class ClienteResponse {


    private UUID id;


    private String nombre;


    private String apellido;


    private String documento;


    private String telefono;


    private String email;


    private String direccion;


    private Boolean activo;

}