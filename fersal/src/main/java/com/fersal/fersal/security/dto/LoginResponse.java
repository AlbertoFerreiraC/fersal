package com.fersal.fersal.security.dto;


import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class LoginResponse {


    private String token;


    private String email;


    private String name;


    private String empresaId;


    private String empresaNombre;

}