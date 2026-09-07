package com.fersal.fersal.cliente.dto;


import lombok.Data;

import java.util.UUID;


@Data
public class ClienteRequest {


    private UUID empresaId;


    private String nombre;


    private String apellido;


    private String documento;


    private String telefono;


    private String email;


    private String direccion;


}