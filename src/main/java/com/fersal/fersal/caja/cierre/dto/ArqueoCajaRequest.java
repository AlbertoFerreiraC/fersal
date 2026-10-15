package com.fersal.fersal.caja.cierre.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ArqueoCajaRequest {
    private UUID empresaId;
    private UUID sucursalId;
    private UUID usuarioId;
    private BigDecimal saldoEsperado;
    private BigDecimal saldoFisico;
}