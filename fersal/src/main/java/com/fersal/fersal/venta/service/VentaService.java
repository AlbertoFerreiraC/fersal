package com.fersal.fersal.venta.service;


import com.fersal.fersal.venta.dto.*;

import java.util.List;
import java.util.UUID;


public interface VentaService {


    VentaResponse crear(
            VentaRequest request
    );


    List<VentaResponse> listar();


    VentaResponse buscar(
            UUID id
    );


}