package com.fersal.fersal.cliente.controller;


import com.fersal.fersal.cliente.dto.*;
import com.fersal.fersal.cliente.service.ClienteService;


import lombok.RequiredArgsConstructor;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {


    private final ClienteService service;


    @PostMapping
    public ResponseEntity<ClienteResponse> crear(
            @RequestBody ClienteRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.crear(request));

    }


    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listar() {

        return ResponseEntity.ok(
                service.listar()
        );

    }


    @GetMapping("/empresa/{id}")
    public ResponseEntity<List<ClienteResponse>> empresa(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                service.listarEmpresa(id)
        );

    }


    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscar(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                service.buscar(id)
        );

    }


    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> actualizar(
            @PathVariable UUID id,
            @RequestBody ClienteRequest request
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