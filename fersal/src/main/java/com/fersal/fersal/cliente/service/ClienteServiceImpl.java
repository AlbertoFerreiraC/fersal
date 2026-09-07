package com.fersal.fersal.cliente.service;


import com.fersal.fersal.cliente.dto.*;
import com.fersal.fersal.cliente.entity.Cliente;
import com.fersal.fersal.cliente.mapper.ClienteMapper;
import com.fersal.fersal.cliente.repository.ClienteRepository;

import com.fersal.fersal.empresa.repository.EmpresaRepository;


import lombok.RequiredArgsConstructor;


import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ClienteServiceImpl
        implements ClienteService {


    private final ClienteRepository repository;


    private final EmpresaRepository empresaRepository;


    private final ClienteMapper mapper;


    @Override
    public ClienteResponse crear(
            ClienteRequest request
    ) {


        Cliente cliente =
                Cliente.builder()

                        .empresa(
                                empresaRepository
                                        .findById(request.getEmpresaId())
                                        .orElseThrow()
                        )

                        .nombre(request.getNombre())

                        .apellido(request.getApellido())

                        .documento(request.getDocumento())

                        .telefono(request.getTelefono())

                        .email(request.getEmail())

                        .direccion(request.getDireccion())

                        .activo(true)

                        .build();


        return mapper.toResponse(
                repository.save(cliente)
        );

    }


    @Override
    public List<ClienteResponse> listar() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();

    }


    @Override
    public List<ClienteResponse> listarEmpresa(
            UUID empresaId
    ) {

        return repository
                .findByEmpresaId(empresaId)
                .stream()
                .map(mapper::toResponse)
                .toList();

    }


    @Override
    public ClienteResponse buscar(UUID id) {

        return mapper.toResponse(
                repository.findById(id)
                        .orElseThrow()
        );

    }


    @Override
    public ClienteResponse actualizar(
            UUID id,
            ClienteRequest request
    ) {

        Cliente cliente =
                repository.findById(id)
                        .orElseThrow();


        cliente.setNombre(
                request.getNombre()
        );

        cliente.setApellido(
                request.getApellido()
        );

        cliente.setDocumento(
                request.getDocumento()
        );

        cliente.setTelefono(
                request.getTelefono()
        );

        cliente.setEmail(
                request.getEmail()
        );

        cliente.setDireccion(
                request.getDireccion()
        );


        return mapper.toResponse(
                repository.save(cliente)
        );

    }


    @Override
    public void eliminar(UUID id) {

        Cliente cliente =
                repository.findById(id)
                        .orElseThrow();


        cliente.setActivo(false);


        repository.save(cliente);

    }


}