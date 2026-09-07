package com.fersal.fersal.caja.cierre.service;


import com.fersal.fersal.caja.cierre.dto.CierreCajaRequest;
import com.fersal.fersal.caja.cierre.dto.CierreCajaResponse;


import java.util.UUID;


public interface CierreCajaService {


    CierreCajaResponse cerrar(
            CierreCajaRequest request
    );


    CierreCajaResponse buscar(
            UUID id
    );


}