package com.fersal.fersal.caja.cierre.dto;


import lombok.Data;


import java.math.BigDecimal;
import java.util.UUID;


@Data
public class CierreCajaRequest {


    private UUID empresaId;


    private UUID sucursalId;


    private UUID usuarioId;


    private BigDecimal saldoInicial;


}