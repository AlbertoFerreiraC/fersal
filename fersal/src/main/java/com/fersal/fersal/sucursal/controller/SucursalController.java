package com.fersal.fersal.sucursal.controller;


import com.fersal.fersal.sucursal.dto.SucursalRequest;
import com.fersal.fersal.sucursal.dto.SucursalResponse;

import com.fersal.fersal.sucursal.service.SucursalService;


import jakarta.validation.Valid;


import lombok.RequiredArgsConstructor;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/sucursales")
@RequiredArgsConstructor
public class SucursalController {


    private final SucursalService service;


    @GetMapping
    public ResponseEntity<List<SucursalResponse>> listar() {

        return ResponseEntity.ok(
                service.listar()
        );

    }


    @GetMapping("/{id}")
    public ResponseEntity<SucursalResponse> buscar(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                service.buscar(id)
        );

    }


    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<SucursalResponse>> listarPorEmpresa(
            @PathVariable UUID empresaId
    ) {

        return ResponseEntity.ok(
                service.listarPorEmpresa(empresaId)
        );

    }


    @PostMapping
    public ResponseEntity<SucursalResponse> crear(
            @Valid @RequestBody SucursalRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.crear(request));

    }


    @PutMapping("/{id}")
    public ResponseEntity<SucursalResponse> actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody SucursalRequest request
    ) {

        return ResponseEntity.ok(
                service.actualizar(id, request)
        );

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID id
    ) {

        service.eliminar(id);

        return ResponseEntity.noContent().build();

    }


}