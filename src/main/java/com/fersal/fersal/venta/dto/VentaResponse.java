package com.fersal.fersal.venta.dto;


import lombok.Builder;
import lombok.Data;


import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@Data
@Builder
public class VentaResponse {

    private UUID id;

    private String numero;

    private String cliente;

    private String documento;

    private String telefono;

    private String direccion;

    private String empresa;

    private String empresaRuc;

    private String empresaTelefono;

    private String fecha;

    private String observacion;

    private BigDecimal total;

    private String estado;

    private String tipoPago;

    private List<DetalleVentaResponse> detalles;

    private String motivoAnulacion;

    private String fechaAnulacion;

}