package com.fersal.fersal.caja.cierre.service;


import com.fersal.fersal.caja.cierre.dto.*;
import com.fersal.fersal.caja.cierre.entity.CierreCaja;
import com.fersal.fersal.caja.cierre.repository.CierreCajaRepository;


import com.fersal.fersal.caja.movimiento.repository.MovimientoCajaRepository;


import com.fersal.fersal.empresa.repository.EmpresaRepository;
import com.fersal.fersal.sucursal.repository.SucursalRepository;
import com.fersal.fersal.security.repository.UsuarioRepository;


import lombok.RequiredArgsConstructor;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CierreCajaServiceImpl
        implements CierreCajaService {


    private final CierreCajaRepository repository;


    private final MovimientoCajaRepository movimientoRepository;


    private final EmpresaRepository empresaRepository;


    private final SucursalRepository sucursalRepository;


    private final UsuarioRepository usuarioRepository;


    @Override
    @Transactional
    public CierreCajaResponse cerrar(
            CierreCajaRequest request
    ) {


        BigDecimal ingresos =
                movimientoRepository
                        .sumBySucursalIdAndTipo(
                                request.getSucursalId(),
                                "INGRESO"
                        );


        if (ingresos == null) {

            ingresos = BigDecimal.ZERO;

        }


        BigDecimal egresos =
                movimientoRepository
                        .sumBySucursalIdAndTipo(
                                request.getSucursalId(),
                                "SALIDA"
                        );


        if (egresos == null) {

            egresos = BigDecimal.ZERO;

        }


        BigDecimal saldoFinal =
                request.getSaldoInicial()
                        .add(ingresos)
                        .subtract(egresos);


        CierreCaja cierre =
                CierreCaja.builder()

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


                        .saldoInicial(
                                request.getSaldoInicial()
                        )


                        .totalIngresos(
                                ingresos
                        )


                        .totalEgresos(
                                egresos
                        )


                        .saldoFinal(
                                saldoFinal
                        )


                        .build();


        CierreCaja guardado =
                repository.save(cierre);


        return CierreCajaResponse.builder()

                .id(guardado.getId())

                .saldoInicial(
                        guardado.getSaldoInicial()
                )

                .totalIngresos(
                        guardado.getTotalIngresos()
                )

                .totalEgresos(
                        guardado.getTotalEgresos()
                )

                .saldoFinal(
                        guardado.getSaldoFinal()
                )

                .fecha(
                        guardado.getFecha()
                )

                .build();

    }


    @Override
    public CierreCajaResponse buscar(UUID id) {

        CierreCaja cierre =
                repository.findById(id)
                        .orElseThrow();


        return CierreCajaResponse.builder()

                .id(cierre.getId())

                .saldoInicial(
                        cierre.getSaldoInicial()
                )

                .totalIngresos(
                        cierre.getTotalIngresos()
                )

                .totalEgresos(
                        cierre.getTotalEgresos()
                )

                .saldoFinal(
                        cierre.getSaldoFinal()
                )

                .fecha(
                        cierre.getFecha()
                )

                .build();

    }


}