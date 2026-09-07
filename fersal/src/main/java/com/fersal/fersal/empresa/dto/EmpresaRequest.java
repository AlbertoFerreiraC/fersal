package com.fersal.fersal.empresa.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class EmpresaRequest {


    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;


    private String ruc;


    private String telefono;


    private String email;


    private String direccion;

}