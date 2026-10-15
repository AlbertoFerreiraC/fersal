package com.fersal.fersal.catalogo.producto.controller;

import com.fersal.fersal.catalogo.producto.dto.ProductoRequest;
import com.fersal.fersal.catalogo.producto.dto.ProductoResponse;
import com.fersal.fersal.catalogo.producto.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService service;

    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@RequestBody ProductoRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<ProductoResponse>> listar(
            @RequestParam(required = false) UUID empresaId
    ) {
        if (empresaId != null) {
            return ResponseEntity.ok(service.listarEmpresa(empresaId));
        }
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<ProductoResponse>> empresa(@PathVariable UUID empresaId) {
        return ResponseEntity.ok(service.listarEmpresa(empresaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> buscar(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscar(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> actualizar(
            @PathVariable UUID id,
            @RequestBody ProductoRequest request
    ) {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @PostMapping(
            value = "/{id}/imagen",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ProductoResponse> subirImagen(
            @PathVariable UUID id,
            @RequestParam("imagen") MultipartFile imagen
    ) {
        return ResponseEntity.ok(service.subirImagen(id, imagen));
    }

    @DeleteMapping("/{id}/imagen")
    public ResponseEntity<Void> eliminarImagen(@PathVariable UUID id) {
        service.eliminarImagen(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}