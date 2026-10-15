package com.fersal.fersal.dashboard.controller;

import com.fersal.fersal.dashboard.dto.DashboardResponse;
import com.fersal.fersal.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService service;

    @GetMapping
    public ResponseEntity<DashboardResponse> obtenerMetricas(
            @RequestParam UUID empresaId,
            @RequestParam UUID sucursalId) {
        return ResponseEntity.ok(service.obtenerMetricas(empresaId, sucursalId));
    }
}