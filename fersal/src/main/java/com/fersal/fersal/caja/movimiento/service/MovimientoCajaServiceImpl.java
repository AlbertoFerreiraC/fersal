package com.fersal.fersal.caja.movimiento.service;


import com.fersal.fersal.caja.movimiento.dto.MovimientoCajaRequest;
import com.fersal.fersal.caja.movimiento.dto.MovimientoCajaResponse;
import com.fersal.fersal.caja.movimiento.entity.MovimientoCaja;
import com.fersal.fersal.caja.movimiento.mapper.MovimientoCajaMapper;
import com.fersal.fersal.caja.movimiento.repository.MovimientoCajaRepository;


import com.fersal.fersal.empresa.repository.EmpresaRepository;
import com.fersal.fersal.sucursal.repository.SucursalRepository;
import com.fersal.fersal.security.repository.UsuarioRepository;


import lombok.RequiredArgsConstructor;


import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class MovimientoCajaServiceImpl
        implements MovimientoCajaService {


    private final MovimientoCajaRepository repository;


    private final EmpresaRepository empresaRepository;


    private final SucursalRepository sucursalRepository;


    private final UsuarioRepository usuarioRepository;


    private final MovimientoCajaMapper mapper;


    @Override
    public MovimientoCajaResponse crear(
            MovimientoCajaRequest request
    ) {


        MovimientoCaja movimiento =
                MovimientoCaja.builder()


                        .empresa(
                                empresaRepository
                                        .findById(request.getEmpresaId())
                                        .orElseThrow()
                        )


                        .sucursal(
                                sucursalRepository
                                        .findById(request.getSucursalId())
                                        .orElseThrow()
                        )


                        .usuario(
                                usuarioRepository
                                        .findById(request.getUsuarioId())
                                        .orElseThrow()
                        )


                        .tipo(
                                request.getTipo()
                        )


                        .monto(
                                request.getMonto()
                        )


                        .descripcion(
                                request.getDescripcion()
                        )


                        .build();


        return mapper.toResponse(
                repository.save(movimiento)
        );


    }


    @Override
    public List<MovimientoCajaResponse> listar() {


        return repository.findAll()

                .stream()

                .map(mapper::toResponse)

                .toList();

    }


    @Override
    public MovimientoCajaResponse buscar(UUID id) {


        return mapper.toResponse(

                repository.findById(id)
                        .orElseThrow()

        );

    }


}