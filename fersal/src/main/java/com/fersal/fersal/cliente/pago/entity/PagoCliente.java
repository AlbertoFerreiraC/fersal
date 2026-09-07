package com.fersal.fersal.cliente.pago.entity;


import com.fersal.fersal.cliente.cuenta.entity.CuentaCorriente;
import com.fersal.fersal.cliente.entity.Cliente;
import com.fersal.fersal.empresa.entity.Empresa;


import jakarta.persistence.*;

import lombok.*;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "pagos_clientes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagoCliente {


    @Id
    @GeneratedValue
    private UUID id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_corriente_id", nullable = false)
    private CuentaCorriente cuentaCorriente;


    @Column(
            precision = 12,
            scale = 2,
            nullable = false
    )
    private BigDecimal monto;


    private String observacion;


    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;


    @Column(name = "created_at")
    private LocalDateTime createdAt;


    @PrePersist
    public void prePersist() {

        createdAt = LocalDateTime.now();

        fechaPago = LocalDateTime.now();

    }


}