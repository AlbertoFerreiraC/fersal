package com.fersal.fersal.cliente.pago.dto;


import lombok.Builder;
import lombok.Data;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Builder
public class PagoClienteResponse {

    private UUID id;

    private UUID clienteId;

    private String cliente;

    private String apellido;

    private String telefono;

    private BigDecimal monto;

    private LocalDateTime fechaPago;

    private String observacion;

}