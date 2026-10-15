package com.fersal.fersal.sucursal.mapper;


import com.fersal.fersal.sucursal.dto.SucursalRequest;
import com.fersal.fersal.sucursal.dto.SucursalResponse;
import com.fersal.fersal.sucursal.entity.Sucursal;

import org.springframework.stereotype.Component;


@Component
public class SucursalMapper {


    public SucursalResponse toResponse(Sucursal sucursal) {


        return SucursalResponse.builder()

                .id(sucursal.getId())

                .empresaId(
                        sucursal.getEmpresa().getId()
                )

                .empresaNombre(
                        sucursal.getEmpresa().getNombre()
                )

                .nombre(
                        sucursal.getNombre()
                )

                .direccion(
                        sucursal.getDireccion()
                )

                .telefono(
                        sucursal.getTelefono()
                )

                .activo(
                        sucursal.getActivo()
                )

                .build();

    }


}