package com.fersal.fersal.caja.cierre.mapper;


import com.fersal.fersal.caja.cierre.dto.CierreCajaResponse;
import com.fersal.fersal.caja.cierre.entity.CierreCaja;


import org.springframework.stereotype.Component;


@Component
public class CierreCajaMapper {


    public CierreCajaResponse toResponse(
            CierreCaja cierre
    ) {


        return CierreCajaResponse.builder()

                .id(
                        cierre.getId()
                )


                .empresaId(
                        cierre.getEmpresa().getId()
                )


                .sucursalId(
                        cierre.getSucursal().getId()
                )


                .usuarioId(
                        cierre.getUsuario().getId()
                )


                .saldoInicial(
                        cierre.getSaldoInicial()
                )


                .totalIngresos(
                        cierre.getTotalIngresos()
                )


                .totalEgresos(
                        cierre.getTotalEgresos()
                )


                .saldoFinal(
                        cierre.getSaldoFinal()
                )


                .fecha(
                        cierre.getFecha()
                )


                .build();

    }

}