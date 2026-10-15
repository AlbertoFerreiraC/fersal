package com.fersal.fersal.cliente.cuenta.controller;

import com.fersal.fersal.cliente.cuenta.dto.CuentaCorrienteRequest;
import com.fersal.fersal.cliente.cuenta.dto.CuentaCorrienteResponse;
import com.fersal.fersal.cliente.cuenta.service.CuentaCorrienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cuentas-corrientes")
@RequiredArgsConstructor
public class CuentaCorrienteController {

    private final CuentaCorrienteService service;

    @PostMapping
    public ResponseEntity<CuentaCorrienteResponse> crear(@RequestBody CuentaCorrienteRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<CuentaCorrienteResponse>> listar(
            @RequestParam(required = false) UUID empresaId
    ) {
        if (empresaId != null) {
            return ResponseEntity.ok(service.listarEmpresa(empresaId));
        }
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<CuentaCorrienteResponse>> listarEmpresa(@PathVariable UUID empresaId) {
        return ResponseEntity.ok(service.listarEmpresa(empresaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuentaCorrienteResponse> buscar(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscar(id));
    }

    @GetMapping("/cliente/{id}")
    public ResponseEntity<CuentaCorrienteResponse> cliente(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscarCliente(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CuentaCorrienteResponse> actualizar(
            @PathVariable UUID id,
            @RequestBody CuentaCorrienteRequest request
    ) {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}