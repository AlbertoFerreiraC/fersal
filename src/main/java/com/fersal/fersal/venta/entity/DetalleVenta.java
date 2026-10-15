package com.fersal.fersal.venta.entity;


import com.fersal.fersal.catalogo.producto.entity.Producto;


import jakarta.persistence.*;

import lombok.*;


import java.math.BigDecimal;
import java.util.UUID;


@Entity
@Table(name = "detalle_ventas")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetalleVenta {


    @Id
    @GeneratedValue
    private UUID id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id")
    private Venta venta;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id")
    private Producto producto;


    private Integer cantidad;


    @Column(name = "precio_unitario")
    private BigDecimal precioUnitario;


    private BigDecimal subtotal;


}