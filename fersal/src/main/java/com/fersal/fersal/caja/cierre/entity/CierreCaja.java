package com.fersal.fersal.caja.cierre.entity;

import com.fersal.fersal.empresa.entity.Empresa;
import com.fersal.fersal.security.entity.Usuario;
import com.fersal.fersal.sucursal.entity.Sucursal;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cierres_caja")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CierreCaja {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sucursal_id")
    private Sucursal sucursal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private BigDecimal saldoInicial;

    private BigDecimal totalIngresos;

    private BigDecimal totalEgresos;

    private BigDecimal saldoFinal;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoCaja estado;

    @Column(name = "fecha_apertura")
    private LocalDateTime fechaApertura;

    @Column(name = "fecha_cierre")
    private LocalDateTime fechaCierre;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (fecha == null) {
            fecha = now;
        }
        if (fechaApertura == null) {
            fechaApertura = now;
        }
        if (estado == null) {
            estado = EstadoCaja.ABIERTA;
        }
    }
}