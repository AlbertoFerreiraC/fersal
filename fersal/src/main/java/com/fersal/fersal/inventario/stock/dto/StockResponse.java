package com.fersal.fersal.inventario.stock.dto;


import lombok.Builder;
import lombok.Data;


import java.util.UUID;


@Data
@Builder
public class StockResponse {


    private UUID id;


    private UUID sucursalId;


    private String sucursalNombre;


    private UUID productoId;


    private String productoNombre;


    private Integer cantidad;


    private Integer stockMinimo;


}