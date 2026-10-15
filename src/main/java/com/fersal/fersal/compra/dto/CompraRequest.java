package com.fersal.fersal.compra.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class CompraRequest {
    private UUID empresaId;
    private UUID sucursalId;
    private UUID usuarioId;
    private UUID proveedorId;
    private String numeroFactura;
    private String condicionPago; // CONTADO o CREDITO
    private boolean pagarDesdeCaja; // Si es contado, si descuenta de caja chica
    private String observacion;
    private List<DetalleCompraItem> detalles;

    @Data
    public static class DetalleCompraItem {
        private UUID productoId;
        private Integer cantidad;
        private BigDecimal precioUnitario;
    }
}