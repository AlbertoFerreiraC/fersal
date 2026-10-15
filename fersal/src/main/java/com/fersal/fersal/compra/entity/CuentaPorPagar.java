package com.fersal.fersal.compra.cuenta.entity;

import com.fersal.fersal.catalogo.proveedor.entity.Proveedor;
import com.fersal.fersal.compra.entity.Compra;
import com.fersal.fersal.empresa.entity.Empresa;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cuentas_por_pagar")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CuentaPorPagar {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id", nullable = false)
    private Proveedor proveedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compra_id", nullable = false)
    private Compra compra;

    @Column(name = "monto_total", precision = 12, scale = 2, nullable = false)
    private BigDecimal montoTotal;

    @Column(name = "saldo_pendiente", precision = 12, scale = 2, nullable = false)
    private BigDecimal saldoPendiente;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDateTime fechaEmision;

    @Column(name = "fecha_vencimiento")
    private LocalDateTime fechaVencimiento;

    @Column(nullable = false)
    private String estado; // PENDIENTE / PAGADO

    @Builder.Default
    private Boolean activo = true;

    @PrePersist
    public void prePersist() {
        if (fechaEmision == null) fechaEmision = LocalDateTime.now();
        if (estado == null) estado = "PENDIENTE";
        if (activo == null) activo = true;
    }
}