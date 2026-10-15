package com.fersal.fersal.cliente.cuenta.entity;


import com.fersal.fersal.cliente.entity.Cliente;
import com.fersal.fersal.empresa.entity.Empresa;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "cuentas_corrientes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CuentaCorriente {


    @Id
    @GeneratedValue
    private UUID id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;


    @Column(
            precision = 12,
            scale = 2
    )
    private BigDecimal saldo;


    @Column(
            name = "limite_credito",
            precision = 12,
            scale = 2
    )
    private BigDecimal limiteCredito;


    private Boolean activo;


    @Column(name = "created_at")
    private LocalDateTime createdAt;


    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @PrePersist
    public void prePersist() {

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (saldo == null) {
            saldo = BigDecimal.ZERO;
        }

        if (activo == null) {
            activo = true;
        }

    }


    @PreUpdate
    public void preUpdate() {

        updatedAt = LocalDateTime.now();

    }

}