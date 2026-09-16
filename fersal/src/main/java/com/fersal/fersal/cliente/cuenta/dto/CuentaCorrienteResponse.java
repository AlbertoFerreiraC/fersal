package com.fersal.fersal.cliente.cuenta.dto;


import lombok.Builder;
import lombok.Data;


import java.math.BigDecimal;
import java.util.UUID;


@Data
@Builder
public class CuentaCorrienteResponse {

    private UUID id;

    private UUID clienteId;

    private String cliente;

    private String apellido;

    private String telefono;

    private BigDecimal saldo;

    private BigDecimal limiteCredito;

    private Boolean activo;

}