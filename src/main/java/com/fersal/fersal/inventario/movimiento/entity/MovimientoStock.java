package com.fersal.fersal.inventario.movimiento.entity;


import com.fersal.fersal.empresa.entity.Empresa;
import com.fersal.fersal.sucursal.entity.Sucursal;

import com.fersal.fersal.catalogo.producto.entity.Producto;

import com.fersal.fersal.security.entity.Usuario;


import jakarta.persistence.*;

import lombok.*;


import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "movimientos_stock")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoStock {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @ManyToOne(fetch = FetchType.LAZY)
    private Empresa empresa;


    @ManyToOne(fetch = FetchType.LAZY)
    private Sucursal sucursal;


    @ManyToOne(fetch = FetchType.LAZY)
    private Producto producto;


    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuario;


    private String tipoMovimiento;


    private Integer cantidad;


    private Integer stockAnterior;


    private Integer stockNuevo;


    private String observacion;


    private LocalDateTime fecha;


    @PrePersist
    public void prePersist() {

        fecha = LocalDateTime.now();

    }

}