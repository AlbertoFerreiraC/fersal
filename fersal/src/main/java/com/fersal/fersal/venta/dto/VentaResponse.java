package com.fersal.fersal.venta.dto;


import lombok.Builder;
import lombok.Data;


import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@Data
@Builder
public class VentaResponse {


    private UUID id;


    private String numero;


    private String cliente;


    private BigDecimal total;


    private String estado;


    private String tipoPago;


    private List<DetalleVentaResponse> detalles;


}