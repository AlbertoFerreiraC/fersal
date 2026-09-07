package com.fersal.fersal.caja.movimiento.dto;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Builder
public class MovimientoCajaResponse {


    private UUID id;


    private String tipo;


    private BigDecimal monto;


    private String descripcion;


    private LocalDateTime fecha;


    private UUID empresaId;


    private UUID sucursalId;


    private UUID usuarioId;


}