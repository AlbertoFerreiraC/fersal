package com.fersal.fersal.venta.comprobante.service;


import com.fersal.fersal.venta.comprobante.dto.ComprobanteResponse;


import java.util.UUID;


public interface ComprobanteService {


    ComprobanteResponse generar(
            UUID ventaId
    );


}