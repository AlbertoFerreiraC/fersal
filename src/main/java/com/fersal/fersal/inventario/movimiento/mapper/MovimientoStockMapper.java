package com.fersal.fersal.inventario.movimiento.mapper;


import com.fersal.fersal.inventario.movimiento.dto.MovimientoResponse;
import com.fersal.fersal.inventario.movimiento.entity.MovimientoStock;


import org.springframework.stereotype.Component;


@Component
public class MovimientoStockMapper {


    public MovimientoResponse toResponse(
            MovimientoStock m
    ) {


        return MovimientoResponse.builder()

                .id(m.getId())

                .producto(
                        m.getProducto().getNombre()
                )

                .tipoMovimiento(
                        m.getTipoMovimiento()
                )

                .cantidad(
                        m.getCantidad()
                )

                .stockAnterior(
                        m.getStockAnterior()
                )

                .stockNuevo(
                        m.getStockNuevo()
                )

                .observacion(
                        m.getObservacion()
                )

                .build();

    }

}