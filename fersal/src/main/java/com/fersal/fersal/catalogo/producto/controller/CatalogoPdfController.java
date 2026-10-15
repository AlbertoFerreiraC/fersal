package com.fersal.fersal.catalogo.producto.controller;

import com.fersal.fersal.catalogo.producto.dto.CatalogoPdfResponse;
import com.fersal.fersal.catalogo.producto.service.CatalogoPdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/productos/catalogo-pdf")
@RequiredArgsConstructor
public class CatalogoPdfController {

    private final CatalogoPdfService catalogoPdfService;

    @GetMapping
    public ResponseEntity<CatalogoPdfResponse> obtenerCatalogoPdf(
            @RequestParam UUID empresaId,
            @RequestParam UUID sucursalId
    ) {
        CatalogoPdfResponse response = catalogoPdfService.generarCatalogoPdf(empresaId, sucursalId);
        return ResponseEntity.ok(response);
    }
}