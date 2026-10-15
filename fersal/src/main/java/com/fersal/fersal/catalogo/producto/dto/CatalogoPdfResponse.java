package com.fersal.fersal.catalogo.producto.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CatalogoPdfResponse {
    private String nombreArchivo;
    private String contenidoBase64;
}