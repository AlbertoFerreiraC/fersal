package com.fersal.fersal.cliente.pago.service;


import com.fersal.fersal.cliente.cuenta.entity.CuentaCorriente;
import com.fersal.fersal.cliente.cuenta.repository.CuentaCorrienteRepository;
import com.fersal.fersal.cliente.entity.Cliente;
import com.fersal.fersal.cliente.pago.dto.PagoClienteRequest;
import com.fersal.fersal.cliente.pago.dto.PagoClienteResponse;
import com.fersal.fersal.cliente.pago.entity.PagoCliente;
import com.fersal.fersal.cliente.pago.repository.PagoClienteRepository;
import com.fersal.fersal.cliente.repository.ClienteRepository;
import com.fersal.fersal.empresa.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class PagoClienteServiceImpl
        implements PagoClienteService {


    private final PagoClienteRepository repository;


    private final CuentaCorrienteRepository cuentaRepository;


    private final ClienteRepository clienteRepository;


    private final EmpresaRepository empresaRepository;


    private PagoClienteResponse map(
            PagoCliente pago
    ) {

        return PagoClienteResponse.builder()

                .id(pago.getId())

                .clienteId(
                        pago.getCliente().getId()
                )

                .cliente(
                        pago.getCliente().getNombre()
                )

                .apellido(
                        pago.getCliente().getApellido()
                )

                .telefono(
                        pago.getCliente().getTelefono()
                )

                .monto(
                        pago.getMonto()
                )

                .fechaPago(
                        pago.getFechaPago()
                )

                .observacion(
                        pago.getObservacion()
                )

                .build();

    }


    @Override
    public PagoClienteResponse crear(
            PagoClienteRequest request
    ) {


        CuentaCorriente cuenta =
                cuentaRepository
                        .findById(
                                request.getCuentaCorrienteId()
                        )
                        .orElseThrow();


        if (cuenta.getSaldo()
                .compareTo(request.getMonto()) < 0) {


            throw new RuntimeException(
                    "El pago supera la deuda actual"
            );

        }


        cuenta.setSaldo(
                cuenta.getSaldo()
                        .subtract(request.getMonto())
        );


        cuentaRepository.save(cuenta);


        Cliente cliente =
                clienteRepository
                        .findById(request.getClienteId())
                        .orElseThrow();


        PagoCliente pago =
                PagoCliente.builder()

                        .empresa(
                                empresaRepository
                                        .findById(request.getEmpresaId())
                                        .orElseThrow()
                        )

                        .cliente(cliente)

                        .cuentaCorriente(cuenta)

                        .monto(
                                request.getMonto()
                        )

                        .observacion(
                                request.getObservacion()
                        )

                        .build();


        return map(
                repository.save(pago)
        );

    }


    @Override
    public List<PagoClienteResponse> listar() {


        return repository.findAll()
                .stream()
                .map(this::map)
                .toList();

    }


    @Override
    public List<PagoClienteResponse> listarCliente(
            UUID clienteId
    ) {

        return repository
                .findByClienteId(clienteId)
                .stream()
                .map(this::map)
                .toList();

    }


    @Override
    public List<PagoClienteResponse> listarCuenta(
            UUID cuentaId
    ) {

        return repository
                .findByCuentaCorrienteId(cuentaId)
                .stream()
                .map(this::map)
                .toList();

    }


    @Override
    public PagoClienteResponse buscar(
            UUID id
    ) {

        return map(
                repository.findById(id)
                        .orElseThrow()
        );

    }


}