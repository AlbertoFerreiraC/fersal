package com.fersal.fersal.inventario.stock.entity;


import com.fersal.fersal.catalogo.producto.entity.Producto;
import com.fersal.fersal.empresa.entity.Empresa;
import com.fersal.fersal.sucursal.entity.Sucursal;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;


@Entity
@Table(
        name = "stock",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "sucursal_id",
                                "producto_id"
                        })
        })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stock {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sucursal_id")
    private Sucursal sucursal;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id")
    private Producto producto;


    private Integer cantidad;


    private Integer stockMinimo;


}