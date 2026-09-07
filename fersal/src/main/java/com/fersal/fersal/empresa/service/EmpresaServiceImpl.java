package com.fersal.fersal.empresa.service;


import com.fersal.fersal.exception.ResourceNotFoundException;
import com.fersal.fersal.empresa.dto.EmpresaRequest;
import com.fersal.fersal.empresa.dto.EmpresaResponse;
import com.fersal.fersal.empresa.entity.Empresa;
import com.fersal.fersal.empresa.mapper.EmpresaMapper;
import com.fersal.fersal.empresa.repository.EmpresaRepository;


import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class EmpresaServiceImpl implements EmpresaService {


    private final EmpresaRepository repository;

    private final EmpresaMapper mapper;


    @Override
    public List<EmpresaResponse> listar() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();

    }


    @Override
    public EmpresaResponse buscar(UUID id) {


        Empresa empresa = repository.findById(id)

                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Empresa no encontrada"
                        )
                );


        return mapper.toResponse(empresa);

    }


    @Override
    public EmpresaResponse crear(EmpresaRequest request) {


        Empresa empresa =
                mapper.toEntity(request);


        return mapper.toResponse(
                repository.save(empresa)
        );

    }


    @Override
    public EmpresaResponse actualizar(
            UUID id,
            EmpresaRequest request
    ) {


        Empresa empresa = repository.findById(id)

                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Empresa no encontrada"
                        )
                );


        mapper.updateEntity(
                empresa,
                request
        );


        return mapper.toResponse(
                repository.save(empresa)
        );

    }


    @Override
    public void eliminar(UUID id) {


        Empresa empresa = repository.findById(id)

                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Empresa no encontrada"
                        )
                );


        empresa.setActivo(false);


        repository.save(empresa);

    }

}