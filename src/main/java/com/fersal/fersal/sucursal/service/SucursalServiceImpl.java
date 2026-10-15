package com.fersal.fersal.sucursal.service;


import com.fersal.fersal.empresa.entity.Empresa;
import com.fersal.fersal.empresa.repository.EmpresaRepository;

import com.fersal.fersal.exception.ResourceNotFoundException;

import com.fersal.fersal.sucursal.dto.SucursalRequest;
import com.fersal.fersal.sucursal.dto.SucursalResponse;

import com.fersal.fersal.sucursal.entity.Sucursal;

import com.fersal.fersal.sucursal.mapper.SucursalMapper;
import com.fersal.fersal.sucursal.repository.SucursalRepository;


import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class SucursalServiceImpl implements SucursalService {


    private final SucursalRepository repository;

    private final EmpresaRepository empresaRepository;

    private final SucursalMapper mapper;


    @Override
    public List<SucursalResponse> listar() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();

    }


    @Override
    public List<SucursalResponse> listarPorEmpresa(UUID empresaId) {

        return repository.findByEmpresaId(empresaId)

                .stream()

                .map(mapper::toResponse)

                .toList();

    }


    @Override
    public SucursalResponse buscar(UUID id) {


        Sucursal sucursal =
                repository.findById(id)

                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Sucursal no encontrada"
                                )
                        );


        return mapper.toResponse(sucursal);

    }


    @Override
    public SucursalResponse crear(
            SucursalRequest request
    ) {


        Empresa empresa =
                empresaRepository.findById(
                                request.getEmpresaId()
                        )

                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Empresa no encontrada"
                                )
                        );


        Sucursal sucursal =
                Sucursal.builder()

                        .empresa(empresa)

                        .nombre(request.getNombre())

                        .direccion(request.getDireccion())

                        .telefono(request.getTelefono())

                        .activo(true)

                        .build();


        return mapper.toResponse(
                repository.save(sucursal)
        );

    }


    @Override
    public SucursalResponse actualizar(
            UUID id,
            SucursalRequest request
    ) {


        Sucursal sucursal =
                repository.findById(id)

                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Sucursal no encontrada"
                                )
                        );


        sucursal.setNombre(
                request.getNombre()
        );


        sucursal.setDireccion(
                request.getDireccion()
        );


        sucursal.setTelefono(
                request.getTelefono()
        );


        return mapper.toResponse(
                repository.save(sucursal)
        );


    }


    @Override
    public void eliminar(UUID id) {


        Sucursal sucursal =
                repository.findById(id)

                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Sucursal no encontrada"
                                )
                        );


        sucursal.setActivo(false);


        repository.save(sucursal);


    }


}