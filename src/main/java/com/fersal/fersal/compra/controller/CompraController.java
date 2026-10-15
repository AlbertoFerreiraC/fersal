package com.fersal.fersal.compra.controller;

import com.fersal.fersal.compra.dto.CompraRequest;
import com.fersal.fersal.compra.dto.CompraResponse;
import com.fersal.fersal.compra.service.CompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/compras")
@RequiredArgsConstructor
public class CompraController {

    private final CompraService service;

    @PostMapping
    public ResponseEntity<CompraResponse> registrar(@RequestBody CompraRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrarCompra(request));
    }

    @GetMapping
    public ResponseEntity<List<CompraResponse>> listar(
            @RequestParam UUID empresaId,
            @RequestParam(required = false) UUID sucursalId
    ) {
        return ResponseEntity.ok(service.listarPorEmpresaYSucursal(empresaId, sucursalId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompraResponse> buscar(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscar(id));
    }
}