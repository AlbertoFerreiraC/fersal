package com.fersal.fersal.venta.dto;


import lombok.Builder;
import lombok.Data;


import java.math.BigDecimal;
import java.util.UUID;


@Data
@Builder
public class DetalleVentaResponse {


    private UUID productoId;


    private String producto;


    private Integer cantidad;


    private BigDecimal precio;


    private BigDecimal subtotal;


}