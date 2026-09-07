package com.fersal.fersal.security.dto;


import lombok.Data;


import java.util.UUID;


@Data
public class UsuarioRequest {


    private UUID empresaId;


    private UUID sucursalId;


    private UUID rolId;


    private String nombre;


    private String apellido;


    private String email;


    private String password;


}