package com.fersal.fersal.caja.cierre.controller;


import com.fersal.fersal.caja.cierre.dto.*;
import com.fersal.fersal.caja.cierre.service.CierreCajaService;


import lombok.RequiredArgsConstructor;


import org.springframework.web.bind.annotation.*;


import java.util.UUID;


@RestController
@RequestMapping("/api/caja/cierres")
@RequiredArgsConstructor
public class CierreCajaController {


    private final CierreCajaService service;


    @PostMapping
    public CierreCajaResponse cerrar(
            @RequestBody CierreCajaRequest request
    ) {

        return service.cerrar(request);

    }


    @GetMapping("/{id}")
    public CierreCajaResponse buscar(
            @PathVariable UUID id
    ) {

        return service.buscar(id);

    }


}