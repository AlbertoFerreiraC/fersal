package com.fersal.fersal.catalogo.producto.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductoRequest {

    private UUID empresaId;

    private UUID categoriaId;

    private UUID proveedorId;

    private String codigo;

    private String nombre;

    private String descripcion;

    private BigDecimal precioCompra;

    private BigDecimal precioVenta;

    private Integer stockMinimo;
}