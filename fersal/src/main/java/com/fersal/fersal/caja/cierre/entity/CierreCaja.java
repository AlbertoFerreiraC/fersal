package com.fersal.fersal.caja.cierre.entity;


import com.fersal.fersal.empresa.entity.Empresa;
import com.fersal.fersal.sucursal.entity.Sucursal;
import com.fersal.fersal.security.entity.Usuario;


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


    private LocalDateTime fecha;


    @PrePersist
    public void prePersist() {

        fecha = LocalDateTime.now();

    }

}