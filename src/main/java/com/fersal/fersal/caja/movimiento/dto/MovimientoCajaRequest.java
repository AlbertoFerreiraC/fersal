package com.fersal.fersal.caja.movimiento.dto;


import lombok.Builder;
import lombok.Data;


import java.math.BigDecimal;
import java.util.UUID;


@Data
@Builder
public class MovimientoCajaRequest {


    private UUID empresaId;


    private UUID sucursalId;


    private UUID usuarioId;


    private String tipo;


    private BigDecimal monto;


    private String descripcion;


}