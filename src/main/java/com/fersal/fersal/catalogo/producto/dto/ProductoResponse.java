package com.fersal.fersal.catalogo.producto.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class ProductoResponse {

    private UUID id;

    private UUID empresaId;

    private String empresaNombre;

    private UUID categoriaId;

    private String categoriaNombre;

    private UUID proveedorId;

    private String proveedorNombre;

    private String codigo;

    private String nombre;

    private String descripcion;

    private String imagenUrl;

    private BigDecimal precioCompra;

    private BigDecimal precioVenta;

    private Integer stockMinimo;

    private Boolean activo;
}