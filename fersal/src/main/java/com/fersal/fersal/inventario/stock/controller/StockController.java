package com.fersal.fersal.inventario.stock.controller;


import com.fersal.fersal.inventario.stock.dto.StockRequest;
import com.fersal.fersal.inventario.stock.dto.StockResponse;

import com.fersal.fersal.inventario.stock.service.StockService;


import lombok.RequiredArgsConstructor;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockController {


    private final StockService service;


    @PostMapping
    public ResponseEntity<StockResponse> crear(
            @RequestBody StockRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.crear(request));

    }


    @GetMapping
    public ResponseEntity<List<StockResponse>> listar() {

        return ResponseEntity.ok(
                service.listar()
        );

    }


    @GetMapping("/sucursal/{id}")
    public ResponseEntity<List<StockResponse>> sucursal(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                service.listarSucursal(id)
        );

    }


    @GetMapping("/{id}")
    public ResponseEntity<StockResponse> buscar(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                service.buscar(id)
        );

    }


    @PutMapping("/{id}")
    public ResponseEntity<StockResponse> actualizar(
            @PathVariable UUID id,
            @RequestBody StockRequest request
    ) {

        return ResponseEntity.ok(
                service.actualizar(id, request)
        );

    }

}