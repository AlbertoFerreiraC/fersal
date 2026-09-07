package com.fersal.fersal.inventario.movimiento.dto;


import lombok.Data;


import java.util.UUID;


@Data
public class MovimientoRequest {


    private UUID empresaId;


    private UUID sucursalId;


    private UUID productoId;


    private UUID usuarioId;


    private String tipoMovimiento;


    private Integer cantidad;


    private String observacion;


}