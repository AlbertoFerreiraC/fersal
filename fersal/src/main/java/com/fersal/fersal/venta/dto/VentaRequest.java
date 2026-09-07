package com.fersal.fersal.venta.dto;


import lombok.Data;


import java.util.List;
import java.util.UUID;


@Data
public class VentaRequest {


    private UUID empresaId;


    private UUID sucursalId;


    private UUID usuarioId;


    private UUID clienteId;


    private String tipoPago;


    private String observacion;


    private List<DetalleVentaRequest> detalles;


}