package com.fersal.fersal.catalogo.proveedor.controller;


import com.fersal.fersal.catalogo.proveedor.dto.*;
import com.fersal.fersal.catalogo.proveedor.service.ProveedorService;


import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/proveedores")
@RequiredArgsConstructor
public class ProveedorController {


    private final ProveedorService service;


    @PostMapping
    public ResponseEntity<ProveedorResponse> crear(
            @RequestBody ProveedorRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.crear(request));

    }


    @GetMapping
    public ResponseEntity<List<ProveedorResponse>> listar() {

        return ResponseEntity.ok(
                service.listar()
        );

    }


    @GetMapping("/{id}")
    public ResponseEntity<ProveedorResponse> buscar(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                service.buscar(id)
        );

    }


    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<ProveedorResponse>> empresa(
            @PathVariable UUID empresaId
    ) {

        return ResponseEntity.ok(
                service.listarEmpresa(empresaId)
        );

    }


    @PutMapping("/{id}")
    public ResponseEntity<ProveedorResponse> actualizar(
            @PathVariable UUID id,
            @RequestBody ProveedorRequest request
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