package com.fersal.fersal.caja.cierre.controller;

import com.fersal.fersal.caja.cierre.dto.AperturaCajaRequest;
import com.fersal.fersal.caja.cierre.dto.ArqueoCajaRequest;
import com.fersal.fersal.caja.cierre.dto.CierreCajaRequest;
import com.fersal.fersal.caja.cierre.dto.CierreCajaResponse;
import com.fersal.fersal.caja.cierre.service.CierreCajaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/caja/cierres")
@RequiredArgsConstructor
public class CierreCajaController {

    private final CierreCajaService service;

    @PostMapping("/abrir")
    public CierreCajaResponse abrir(@RequestBody AperturaCajaRequest request) {
        return service.abrir(request);
    }

    @PostMapping("/cerrar")
    public CierreCajaResponse cerrar(@RequestBody CierreCajaRequest request) {
        return service.cerrar(request);
    }

    @PostMapping("/notificar-arqueo")
    public ResponseEntity<Void> notificarArqueo(@RequestBody ArqueoCajaRequest request) {
        service.notificarArqueo(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/estado")
    public CierreCajaResponse obtenerCajaAbierta(
            @RequestParam UUID empresaId,
            @RequestParam UUID sucursalId,
            @RequestParam UUID usuarioId) {
        return service.obtenerCajaAbierta(empresaId, sucursalId, usuarioId);
    }

    @GetMapping
    public List<CierreCajaResponse> listar(
            @RequestParam(required = false) UUID empresaId,
            @RequestParam(required = false) UUID sucursalId,
            @RequestParam(required = false) UUID usuarioId
    ) {
        if (empresaId != null) {
            return service.listarPorEmpresaYSucursal(empresaId, sucursalId, usuarioId);
        }
        return service.listar();
    }

    @GetMapping("/{id}")
    public CierreCajaResponse buscar(@PathVariable UUID id) {
        return service.buscar(id);
    }
}