package com.fersal.fersal.cliente.cuenta.service;


import com.fersal.fersal.cliente.cuenta.dto.*;
import com.fersal.fersal.cliente.cuenta.entity.CuentaCorriente;
import com.fersal.fersal.cliente.cuenta.repository.CuentaCorrienteRepository;

import com.fersal.fersal.cliente.entity.Cliente;
import com.fersal.fersal.cliente.repository.ClienteRepository;

import com.fersal.fersal.empresa.repository.EmpresaRepository;


import lombok.RequiredArgsConstructor;


import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CuentaCorrienteServiceImpl
        implements CuentaCorrienteService {


    private final CuentaCorrienteRepository repository;


    private final ClienteRepository clienteRepository;


    private final EmpresaRepository empresaRepository;


    private CuentaCorrienteResponse map(
            CuentaCorriente cuenta
    ) {


        return CuentaCorrienteResponse.builder()

                .id(cuenta.getId())

                .clienteId(
                        cuenta.getCliente().getId()
                )

                .cliente(
                        cuenta.getCliente().getNombre()
                )

                .apellido(
                        cuenta.getCliente().getApellido()
                )

                .telefono(
                        cuenta.getCliente().getTelefono()
                )

                .saldo(
                        cuenta.getSaldo()
                )

                .limiteCredito(
                        cuenta.getLimiteCredito()
                )

                .activo(
                        cuenta.getActivo()
                )

                .build();

    }


    @Override
    public CuentaCorrienteResponse crear(
            CuentaCorrienteRequest request
    ) {


        Cliente cliente =
                clienteRepository
                        .findById(request.getClienteId())
                        .orElseThrow();


        CuentaCorriente cuenta =
                CuentaCorriente.builder()

                        .empresa(
                                empresaRepository
                                        .findById(request.getEmpresaId())
                                        .orElseThrow()
                        )

                        .cliente(cliente)

                        .saldo(
                                BigDecimal.ZERO
                        )

                        .limiteCredito(
                                request.getLimiteCredito()
                        )

                        .activo(true)

                        .build();


        return map(
                repository.save(cuenta)
        );

    }


    @Override
    public List<CuentaCorrienteResponse> listar() {


        return repository.findAll()
                .stream()
                .map(this::map)
                .toList();

    }


    @Override
    public CuentaCorrienteResponse buscar(
            UUID id
    ) {

        return map(
                repository.findById(id)
                        .orElseThrow()
        );

    }


    @Override
    public CuentaCorrienteResponse buscarCliente(
            UUID clienteId
    ) {


        return map(
                repository.findByClienteId(clienteId)
                        .orElseThrow()
        );

    }


    @Override
    public CuentaCorrienteResponse actualizar(
            UUID id,
            CuentaCorrienteRequest request
    ) {


        CuentaCorriente cuenta =
                repository.findById(id)
                        .orElseThrow();


        cuenta.setLimiteCredito(
                request.getLimiteCredito()
        );


        return map(
                repository.save(cuenta)
        );

    }


    @Override
    public void eliminar(UUID id) {


        CuentaCorriente cuenta =
                repository.findById(id)
                        .orElseThrow();


        cuenta.setActivo(false);


        repository.save(cuenta);

    }


}