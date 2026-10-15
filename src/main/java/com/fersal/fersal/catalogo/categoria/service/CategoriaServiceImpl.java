package com.fersal.fersal.catalogo.categoria.service;


import com.fersal.fersal.catalogo.categoria.dto.*;
import com.fersal.fersal.catalogo.categoria.entity.Categoria;
import com.fersal.fersal.catalogo.categoria.mapper.CategoriaMapper;
import com.fersal.fersal.catalogo.categoria.repository.CategoriaRepository;

import com.fersal.fersal.empresa.repository.EmpresaRepository;


import lombok.RequiredArgsConstructor;


import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl
        implements CategoriaService {


    private final CategoriaRepository repository;

    private final EmpresaRepository empresaRepository;

    private final CategoriaMapper mapper;


    @Override
    public CategoriaResponse crear(
            CategoriaRequest request
    ) {

        Categoria categoria =
                Categoria.builder()

                        .empresa(
                                empresaRepository.findById(
                                        request.getEmpresaId()
                                ).orElseThrow()
                        )

                        .nombre(request.getNombre())

                        .descripcion(request.getDescripcion())

                        .activo(true)

                        .build();


        return mapper.toResponse(
                repository.save(categoria)
        );

    }


    @Override
    public java.util.List<CategoriaResponse> listar() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();

    }


    @Override
    public java.util.List<CategoriaResponse> listarEmpresa(
            java.util.UUID id
    ) {

        return repository.findByEmpresaId(id)
                .stream()
                .map(mapper::toResponse)
                .toList();

    }


    @Override
    public CategoriaResponse buscar(UUID id) {

        return mapper.toResponse(
                repository.findById(id)
                        .orElseThrow()
        );

    }


    @Override
    public CategoriaResponse actualizar(
            UUID id,
            CategoriaRequest request
    ) {

        Categoria c =
                repository.findById(id)
                        .orElseThrow();


        c.setNombre(request.getNombre());

        c.setDescripcion(request.getDescripcion());


        return mapper.toResponse(
                repository.save(c)
        );

    }


    @Override
    public void eliminar(UUID id) {

        Categoria c =
                repository.findById(id)
                        .orElseThrow();


        c.setActivo(false);

        repository.save(c);

    }


}