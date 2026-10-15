package com.fersal.fersal.venta.dto;


import lombok.Data;

import java.util.UUID;


@Data
public class DetalleVentaRequest {


    private UUID productoId;


    private Integer cantidad;


}