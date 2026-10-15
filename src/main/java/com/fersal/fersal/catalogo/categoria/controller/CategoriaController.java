package com.fersal.fersal.catalogo.categoria.controller;

import com.fersal.fersal.catalogo.categoria.dto.CategoriaRequest;
import com.fersal.fersal.catalogo.categoria.dto.CategoriaResponse;
import com.fersal.fersal.catalogo.categoria.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService service;

    @PostMapping
    public ResponseEntity<CategoriaResponse> crear(@Valid @RequestBody CategoriaRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listar(
            @RequestParam(required = false) UUID empresaId
    ) {
        if (empresaId != null) {
            return ResponseEntity.ok(service.listarEmpresa(empresaId));
        }
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<CategoriaResponse>> listarEmpresa(@PathVariable UUID empresaId) {
        return ResponseEntity.ok(service.listarEmpresa(empresaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> buscar(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscar(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> actualizar(
            @PathVariable UUID id,
            @RequestBody CategoriaRequest request
    ) {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}