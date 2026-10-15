package com.fersal.fersal.catalogo.producto.service;

import com.fersal.fersal.catalogo.producto.dto.CatalogoPdfResponse;

import java.util.UUID;

public interface CatalogoPdfService {
    CatalogoPdfResponse generarCatalogoPdf(UUID empresaId, UUID sucursalId);
}