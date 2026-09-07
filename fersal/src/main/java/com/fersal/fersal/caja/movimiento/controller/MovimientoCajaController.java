package com.fersal.fersal.caja.movimiento.controller;


import com.fersal.fersal.caja.movimiento.dto.MovimientoCajaRequest;
import com.fersal.fersal.caja.movimiento.dto.MovimientoCajaResponse;
import com.fersal.fersal.caja.movimiento.service.MovimientoCajaService;


import lombok.RequiredArgsConstructor;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/caja/movimientos")
@RequiredArgsConstructor
public class MovimientoCajaController {


    private final MovimientoCajaService service;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovimientoCajaResponse crear(
            @RequestBody MovimientoCajaRequest request
    ) {

        return service.crear(request);

    }


    @GetMapping
    public List<MovimientoCajaResponse> listar() {

        return service.listar();

    }


    @GetMapping("/{id}")
    public MovimientoCajaResponse buscar(
            @PathVariable UUID id
    ) {

        return service.buscar(id);

    }


}