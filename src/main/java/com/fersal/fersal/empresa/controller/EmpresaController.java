package com.fersal.fersal.empresa.controller;


import com.fersal.fersal.empresa.dto.EmpresaRequest;
import com.fersal.fersal.empresa.dto.EmpresaResponse;
import com.fersal.fersal.empresa.service.EmpresaService;


import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/empresas")
@RequiredArgsConstructor
public class EmpresaController {


    private final EmpresaService service;


    @GetMapping
    public ResponseEntity<List<EmpresaResponse>> listar() {

        return ResponseEntity.ok(
                service.listar()
        );

    }


    @GetMapping("/{id}")
    public ResponseEntity<EmpresaResponse> buscar(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                service.buscar(id)
        );

    }


    @PostMapping
    public ResponseEntity<EmpresaResponse> crear(
            @Valid @RequestBody EmpresaRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.crear(request));

    }


    @PutMapping("/{id}")
    public ResponseEntity<EmpresaResponse> actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody EmpresaRequest request
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