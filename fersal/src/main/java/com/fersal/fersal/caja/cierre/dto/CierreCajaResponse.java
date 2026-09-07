package com.fersal.fersal.caja.cierre.dto;


import lombok.Builder;
import lombok.Data;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Builder
public class CierreCajaResponse {


    private UUID id;


    private UUID empresaId;


    private UUID sucursalId;


    private UUID usuarioId;


    private BigDecimal saldoInicial;


    private BigDecimal totalIngresos;


    private BigDecimal totalEgresos;


    private BigDecimal saldoFinal;


    private LocalDateTime fecha;


}