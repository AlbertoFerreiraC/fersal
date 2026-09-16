package com.fersal.fersal.empresa.mapper;


import com.fersal.fersal.empresa.dto.EmpresaRequest;
import com.fersal.fersal.empresa.dto.EmpresaResponse;
import com.fersal.fersal.empresa.entity.Empresa;

import org.springframework.stereotype.Component;


@Component
public class EmpresaMapper {


    public Empresa toEntity(EmpresaRequest request) {

        return Empresa.builder()

                .nombre(request.getNombre())

                .ruc(request.getRuc())

                .telefono(request.getTelefono())

                .email(request.getEmail())

                .direccion(request.getDireccion())

                .activo(true)

                .build();

    }



    public EmpresaResponse toResponse(Empresa empresa) {

        return EmpresaResponse.builder()

                .id(empresa.getId())

                .nombre(empresa.getNombre())

                .ruc(empresa.getRuc())

                .telefono(empresa.getTelefono())

                .email(empresa.getEmail())

                .direccion(empresa.getDireccion())

                .activo(empresa.getActivo())

                .createdAt(empresa.getCreatedAt())

                .updatedAt(empresa.getUpdatedAt())

                .logoUrl(empresa.getLogoUrl())

                .build();

    }



    public void updateEntity(
            Empresa empresa,
            EmpresaRequest request
    ) {


        empresa.setNombre(request.getNombre());

        empresa.setRuc(request.getRuc());

        empresa.setTelefono(request.getTelefono());

        empresa.setEmail(request.getEmail());

        empresa.setDireccion(request.getDireccion());

    }

}