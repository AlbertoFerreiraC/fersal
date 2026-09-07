package com.fersal.fersal.inventario.stock.dto;


import lombok.Data;

import java.util.UUID;


@Data
public class StockRequest {


    private UUID empresaId;


    private UUID sucursalId;


    private UUID productoId;


    private Integer cantidad;


    private Integer stockMinimo;


}