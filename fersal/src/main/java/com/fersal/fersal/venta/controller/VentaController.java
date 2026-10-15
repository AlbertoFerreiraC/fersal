package com.fersal.fersal.venta.controller;

import com.fersal.fersal.venta.comprobante.dto.ComprobanteResponse;
import com.fersal.fersal.venta.comprobante.service.ComprobanteService;
import com.fersal.fersal.venta.dto.AnularVentaRequest;
import com.fersal.fersal.venta.dto.VentaRequest;
import com.fersal.fersal.venta.dto.VentaResponse;
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
    private final ComprobanteService comprobanteService;

    @PostMapping
    public ResponseEntity<VentaResponse> crear(@RequestBody VentaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<VentaResponse>> listar(
            @RequestParam(required = false) UUID empresaId,
            @RequestParam(required = false) UUID sucursalId
    ) {
        if (empresaId != null) {
            return ResponseEntity.ok(service.listarPorEmpresaYSucursal(empresaId, sucursalId));
        }
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaResponse> buscar(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscar(id));
    }

    @GetMapping("/{id}/comprobante")
    public ResponseEntity<ComprobanteResponse> comprobante(@PathVariable UUID id) {
        return ResponseEntity.ok(comprobanteService.generar(id));
    }

    @PutMapping("/{id}/anular")
    public ResponseEntity<VentaResponse> anular(
            @PathVariable UUID id,
            @RequestParam UUID usuarioId,
            @RequestBody AnularVentaRequest request
    ) {
        return ResponseEntity.ok(service.anular(id, usuarioId, request.getMotivo()));
    }
}