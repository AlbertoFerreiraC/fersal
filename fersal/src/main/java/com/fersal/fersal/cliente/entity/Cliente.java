package com.fersal.fersal.cliente.entity;


import com.fersal.fersal.empresa.entity.Empresa;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "clientes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {


    @Id
    @GeneratedValue
    private UUID id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;


    @Column(nullable = false)
    private String nombre;


    private String apellido;


    private String documento;


    private String telefono;


    private String email;


    private String direccion;


    private Boolean activo = true;


    @Column(name = "created_at")
    private LocalDateTime createdAt;


    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @PrePersist
    public void prePersist() {

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

    }


    @PreUpdate
    public void preUpdate() {

        updatedAt = LocalDateTime.now();

    }

}