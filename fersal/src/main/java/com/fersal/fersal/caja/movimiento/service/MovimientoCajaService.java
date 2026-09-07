package com.fersal.fersal.caja.movimiento.service;


import com.fersal.fersal.caja.movimiento.dto.MovimientoCajaRequest;
import com.fersal.fersal.caja.movimiento.dto.MovimientoCajaResponse;


import java.util.List;
import java.util.UUID;


public interface MovimientoCajaService {


    MovimientoCajaResponse crear(
            MovimientoCajaRequest request
    );


    List<MovimientoCajaResponse> listar();


    MovimientoCajaResponse buscar(
            UUID id
    );


}