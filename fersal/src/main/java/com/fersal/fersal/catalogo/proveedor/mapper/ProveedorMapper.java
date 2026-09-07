package com.fersal.fersal.catalogo.proveedor.mapper;


import com.fersal.fersal.catalogo.proveedor.dto.ProveedorResponse;
import com.fersal.fersal.catalogo.proveedor.entity.Proveedor;


import org.springframework.stereotype.Component;


@Component
public class ProveedorMapper {


    public ProveedorResponse toResponse(
            Proveedor p
    ) {

        return ProveedorResponse.builder()

                .id(p.getId())

                .empresaId(
                        p.getEmpresa().getId()
                )

                .empresaNombre(
                        p.getEmpresa().getNombre()
                )

                .nombre(p.getNombre())

                .ruc(p.getRuc())

                .telefono(p.getTelefono())

                .email(p.getEmail())

                .direccion(p.getDireccion())

                .activo(p.getActivo())

                .build();

    }

}