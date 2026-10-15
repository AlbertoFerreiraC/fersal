package com.fersal.fersal.inventario.movimiento.dto;


import lombok.Builder;
import lombok.Data;


import java.util.UUID;


@Data
@Builder
public class MovimientoResponse {


    private UUID id;


    private String producto;


    private String tipoMovimiento;


    private Integer cantidad;


    private Integer stockAnterior;


    private Integer stockNuevo;


    private String observacion;

}