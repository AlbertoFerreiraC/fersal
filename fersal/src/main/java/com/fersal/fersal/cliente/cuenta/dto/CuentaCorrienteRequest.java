package com.fersal.fersal.cliente.cuenta.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;


@Data
public class CuentaCorrienteRequest {


    private UUID empresaId;


    private UUID clienteId;


    private BigDecimal limiteCredito;


}