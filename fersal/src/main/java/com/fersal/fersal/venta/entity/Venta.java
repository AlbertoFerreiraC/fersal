package com.fersal.fersal.venta.entity;


import com.fersal.fersal.cliente.entity.Cliente;
import com.fersal.fersal.empresa.entity.Empresa;
import com.fersal.fersal.sucursal.entity.Sucursal;
import com.fersal.fersal.security.entity.Usuario;


import jakarta.persistence.*;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "ventas")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Venta {


    @Id
    @GeneratedValue
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


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;


    @Column(nullable = false)
    private String numero;


    private BigDecimal subtotal;


    private BigDecimal descuento;


    private BigDecimal total;


    private String estado;


    @Column(name = "tipo_pago")
    private String tipoPago;


    private String observacion;


    private LocalDateTime fecha;


    @OneToMany(
            mappedBy = "venta",
            cascade = CascadeType.ALL
    )
    private List<DetalleVenta> detalles =
            new ArrayList<>();


    @PrePersist
    public void prePersist() {

        fecha = LocalDateTime.now();

        estado = "PAGADA";

        if (descuento == null)
            descuento = BigDecimal.ZERO;

    }

}