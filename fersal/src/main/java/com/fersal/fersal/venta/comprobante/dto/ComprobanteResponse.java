package com.fersal.fersal.venta.comprobante.dto;


import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ComprobanteResponse {


    private String nombreArchivo;


    private String contenidoBase64;


}