package com.fersal.fersal.dashboard.service;

import com.fersal.fersal.dashboard.dto.DashboardResponse;

import java.util.UUID;

public interface DashboardService {
    DashboardResponse obtenerMetricas(UUID empresaId, UUID sucursalId);
}