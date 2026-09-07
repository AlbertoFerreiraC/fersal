package com.fersal.fersal.security.dto;


import lombok.Builder;
import lombok.Data;


import java.util.UUID;


@Data
@Builder
public class UsuarioResponse {


    private UUID id;


    private String nombre;


    private String apellido;


    private String email;


    private String rol;


    private Boolean activo;


}