package com.fersal.fersal.caja.movimiento.mapper;


import com.fersal.fersal.caja.movimiento.dto.MovimientoCajaResponse;
import com.fersal.fersal.caja.movimiento.entity.MovimientoCaja;


import org.springframework.stereotype.Component;


@Component
public class MovimientoCajaMapper {


    public MovimientoCajaResponse toResponse(
            MovimientoCaja movimiento
    ) {

        return MovimientoCajaResponse.builder()

                .id(movimiento.getId())

                .tipo(movimiento.getTipo())

                .monto(movimiento.getMonto())

                .descripcion(movimiento.getDescripcion())

                .fecha(movimiento.getFecha())


                .empresaId(
                        movimiento.getEmpresa().getId()
                )


                .sucursalId(
                        movimiento.getSucursal().getId()
                )


                .usuarioId(
                        movimiento.getUsuario().getId()
                )


                .build();

    }


}