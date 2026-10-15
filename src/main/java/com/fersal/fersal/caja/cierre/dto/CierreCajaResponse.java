package com.fersal.fersal.caja.cierre.dto;

import com.fersal.fersal.caja.cierre.entity.EstadoCaja;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CierreCajaResponse {
    private UUID id;
    private UUID empresaId;
    private UUID sucursalId;
    private UUID usuarioId;
    private BigDecimal saldoInicial;
    private BigDecimal totalIngresos;
    private BigDecimal totalEgresos;
    private BigDecimal saldoFinal;
    private EstadoCaja estado;
    private LocalDateTime fechaApertura;
    private LocalDateTime fechaCierre;
}