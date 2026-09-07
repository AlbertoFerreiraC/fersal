package com.fersal.fersal.catalogo.proveedor.service;


import com.fersal.fersal.catalogo.proveedor.dto.*;
import com.fersal.fersal.catalogo.proveedor.entity.Proveedor;
import com.fersal.fersal.catalogo.proveedor.mapper.ProveedorMapper;
import com.fersal.fersal.catalogo.proveedor.repository.ProveedorRepository;


import com.fersal.fersal.empresa.repository.EmpresaRepository;


import lombok.RequiredArgsConstructor;


import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ProveedorServiceImpl
        implements ProveedorService {


    private final ProveedorRepository repository;

    private final EmpresaRepository empresaRepository;

    private final ProveedorMapper mapper;


    public ProveedorResponse crear(
            ProveedorRequest request
    ) {

        Proveedor p =
                Proveedor.builder()

                        .empresa(
                                empresaRepository.findById(
                                        request.getEmpresaId()
                                ).orElseThrow()
                        )

                        .nombre(request.getNombre())

                        .ruc(request.getRuc())

                        .telefono(request.getTelefono())

                        .email(request.getEmail())

                        .direccion(request.getDireccion())

                        .activo(true)

                        .build();


        return mapper.toResponse(
                repository.save(p)
        );

    }


    public java.util.List<ProveedorResponse> listar() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();

    }


    public java.util.List<ProveedorResponse> listarEmpresa(
            java.util.UUID id
    ) {

        return repository.findByEmpresaId(id)
                .stream()
                .map(mapper::toResponse)
                .toList();

    }


    public ProveedorResponse buscar(UUID id) {

        return mapper.toResponse(
                repository.findById(id)
                        .orElseThrow()
        );

    }


    public ProveedorResponse actualizar(
            UUID id,
            ProveedorRequest request
    ) {

        Proveedor p =
                repository.findById(id)
                        .orElseThrow();


        p.setNombre(request.getNombre());
        p.setRuc(request.getRuc());
        p.setTelefono(request.getTelefono());
        p.setEmail(request.getEmail());
        p.setDireccion(request.getDireccion());


        return mapper.toResponse(
                repository.save(p)
        );

    }


    public void eliminar(UUID id) {

        Proveedor p =
                repository.findById(id)
                        .orElseThrow();


        p.setActivo(false);


        repository.save(p);

    }

}