package com.fersal.fersal.inventario.movimiento.controller;


import com.fersal.fersal.inventario.movimiento.dto.*;
import com.fersal.fersal.inventario.movimiento.service.MovimientoStockService;


import lombok.RequiredArgsConstructor;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/movimientos-stock")
@RequiredArgsConstructor
public class MovimientoStockController {


    private final MovimientoStockService service;


    @PostMapping
    public ResponseEntity<MovimientoResponse> crear(
            @RequestBody MovimientoRequest request
    ) {


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        service.crear(request)
                );


    }


    @GetMapping
    public ResponseEntity<List<MovimientoResponse>> listar() {


        return ResponseEntity.ok(
                service.listar()
        );


    }


    @GetMapping("/producto/{id}")
    public ResponseEntity<List<MovimientoResponse>> producto(
            @PathVariable UUID id
    ) {


        return ResponseEntity.ok(
                service.listarProducto(id)
        );


    }


}