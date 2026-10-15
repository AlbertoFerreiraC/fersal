package com.fersal.fersal.compra.entity;

import com.fersal.fersal.catalogo.proveedor.entity.Proveedor;
import com.fersal.fersal.empresa.entity.Empresa;
import com.fersal.fersal.security.entity.Usuario;
import com.fersal.fersal.sucursal.entity.Sucursal;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "compras")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sucursal_id", nullable = false)
    private Sucursal sucursal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id", nullable = false)
    private Proveedor proveedor;

    @Column(name = "numero_factura")
    private String numeroFactura;

    @Column(name = "condicion_pago", nullable = false)
    private String condicionPago;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal total;

    @Column(nullable = false)
    private String estado;

    private String observacion;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Builder.Default
    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleCompra> detalles = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (fecha == null) fecha = LocalDateTime.now();
        if (estado == null) estado = "COMPLETADA";
        if (total == null) total = BigDecimal.ZERO;
    }
}