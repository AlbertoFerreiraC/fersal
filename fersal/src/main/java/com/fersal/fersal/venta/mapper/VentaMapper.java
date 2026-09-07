package com.fersal.fersal.venta.mapper;


import com.fersal.fersal.venta.dto.*;
import com.fersal.fersal.venta.entity.*;


import org.springframework.stereotype.Component;


@Component
public class VentaMapper {


    public VentaResponse toResponse(
            Venta venta
    ) {


        return VentaResponse.builder()

                .id(venta.getId())

                .numero(venta.getNumero())

                .cliente(
                        venta.getCliente() != null
                                ? venta.getCliente().getNombre()
                                : "Consumidor Final"
                )

                .total(
                        venta.getTotal()
                )

                .estado(
                        venta.getEstado()
                )

                .tipoPago(
                        venta.getTipoPago()
                )

                .detalles(
                        venta.getDetalles()
                                .stream()
                                .map(detalle ->

                                        DetalleVentaResponse.builder()

                                                .productoId(
                                                        detalle.getProducto().getId()
                                                )

                                                .producto(
                                                        detalle.getProducto().getNombre()
                                                )

                                                .cantidad(
                                                        detalle.getCantidad()
                                                )

                                                .precio(
                                                        detalle.getPrecioUnitario()
                                                )

                                                .subtotal(
                                                        detalle.getSubtotal()
                                                )

                                                .build()

                                )
                                .toList()
                )

                .build();

    }


}