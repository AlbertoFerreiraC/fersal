package com.fersal.fersal.caja.movimiento.entity;


import com.fersal.fersal.empresa.entity.Empresa;
import com.fersal.fersal.sucursal.entity.Sucursal;
import com.fersal.fersal.security.entity.Usuario;


import jakarta.persistence.*;

import lombok.*;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "movimientos_caja")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoCaja {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @ManyToOne(fetch = FetchType.LAZY)
    private Empresa empresa;


    @ManyToOne(fetch = FetchType.LAZY)
    private Sucursal sucursal;


    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuario;


    private String tipo;


    private BigDecimal monto;


    private String descripcion;


    private LocalDateTime fecha;


    @PrePersist
    public void prePersist() {

        fecha = LocalDateTime.now();

    }

}