package com.fersal.fersal.catalogo.categoria.mapper;


import com.fersal.fersal.catalogo.categoria.dto.*;
import com.fersal.fersal.catalogo.categoria.entity.Categoria;


import org.springframework.stereotype.Component;


@Component
public class CategoriaMapper {


    public CategoriaResponse toResponse(
            Categoria c
    ) {

        return CategoriaResponse.builder()

                .id(c.getId())

                .empresaId(
                        c.getEmpresa().getId()
                )

                .empresaNombre(
                        c.getEmpresa().getNombre()
                )

                .nombre(c.getNombre())

                .descripcion(c.getDescripcion())

                .activo(c.getActivo())

                .build();

    }

}