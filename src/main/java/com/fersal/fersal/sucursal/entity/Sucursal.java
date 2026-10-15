package com.fersal.fersal.sucursal.entity;


import com.fersal.fersal.empresa.entity.Empresa;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "sucursales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sucursal {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "empresa_id",
            nullable = false
    )
    private Empresa empresa;


    @Column(nullable = false, length = 100)
    private String nombre;


    @Column(columnDefinition = "TEXT")
    private String direccion;


    @Column(length = 30)
    private String telefono;


    private Boolean activo = true;


    private LocalDateTime createdAt;


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