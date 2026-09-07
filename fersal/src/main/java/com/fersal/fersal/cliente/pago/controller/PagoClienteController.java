package com.fersal.fersal.cliente.pago.controller;


import com.fersal.fersal.cliente.pago.dto.*;
import com.fersal.fersal.cliente.pago.service.PagoClienteService;


import lombok.RequiredArgsConstructor;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/pagos-clientes")
@RequiredArgsConstructor
public class PagoClienteController {


    private final PagoClienteService service;


    @PostMapping
    public ResponseEntity<PagoClienteResponse> crear(
            @RequestBody PagoClienteRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        service.crear(request)
                );

    }


    @GetMapping
    public ResponseEntity<List<PagoClienteResponse>> listar() {

        return ResponseEntity.ok(
                service.listar()
        );

    }


    @GetMapping("/{id}")
    public ResponseEntity<PagoClienteResponse> buscar(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                service.buscar(id)
        );

    }


    @GetMapping("/cliente/{id}")
    public ResponseEntity<List<PagoClienteResponse>> cliente(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                service.listarCliente(id)
        );

    }


    @GetMapping("/cuenta/{id}")
    public ResponseEntity<List<PagoClienteResponse>> cuenta(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                service.listarCuenta(id)
        );

    }


}