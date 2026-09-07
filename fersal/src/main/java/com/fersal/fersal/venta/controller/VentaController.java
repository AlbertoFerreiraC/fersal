package com.fersal.fersal.venta.controller;


import com.fersal.fersal.venta.dto.*;
import com.fersal.fersal.venta.service.VentaService;


import lombok.RequiredArgsConstructor;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
public class VentaController {


    private final VentaService service;


    @PostMapping
    public ResponseEntity<VentaResponse> crear(
            @RequestBody VentaRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        service.crear(request)
                );

    }


    @GetMapping
    public ResponseEntity<List<VentaResponse>> listar() {

        return ResponseEntity.ok(
                service.listar()
        );

    }


    @GetMapping("/{id}")
    public ResponseEntity<VentaResponse> buscar(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                service.buscar(id)
        );

    }


}