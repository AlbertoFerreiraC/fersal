package com.fersal.fersal.empresa.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Builder
public class EmpresaResponse {


    private UUID id;

    private String nombre;

    private String ruc;

    private String telefono;

    private String email;

    private String direccion;

    private Boolean activo;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String logoUrl;

}