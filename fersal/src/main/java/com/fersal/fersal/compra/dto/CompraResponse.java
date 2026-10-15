package com.fersal.fersal.compra.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CompraResponse {
    private UUID id;
    private String numeroFactura;
    private String proveedor;
    private String sucursal;
    private String condicionPago;
    private BigDecimal total;
    private String estado;
    private LocalDateTime fecha;
    private List<DetalleCompraResponse> detalles;

    @Data
    @Builder
    public static class DetalleCompraResponse {
        private UUID productoId;
        private String producto;
        private Integer cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;
    }
}