package com.fersal.fersal.cliente.pago.dto;


import lombok.Data;


import java.math.BigDecimal;
import java.util.UUID;


@Data
public class PagoClienteRequest {


    private UUID empresaId;


    private UUID clienteId;


    private UUID cuentaCorrienteId;


    private BigDecimal monto;


    private String observacion;


}