package com.fersal.fersal.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    // 1. Ventas del Día
    private BigDecimal ventasHoy;
    private Long transaccionesHoy;
    private BigDecimal gananciaHoy;

    // 2. Balances Financieros
    private BigDecimal totalPorCobrar; // Cuentas corrientes fiadas
    private BigDecimal totalPorPagar;  // Pendientes / proveedores

    // 3. Stock Bajo
    private List<StockBajoItem> stockBajo;

    // 4. Últimas Transacciones (Ventas recientes)
    private List<TransaccionRecienteItem> ultimasTransacciones;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StockBajoItem {
        private UUID productoId;
        private String nombre;
        private Integer stockActual;
        private Integer stockMinimo;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransaccionRecienteItem {
        private UUID ventaId;
        private String numero;
        private String cliente;
        private BigDecimal total;
        private String tipoPago;
        private LocalDateTime fecha;
    }
}