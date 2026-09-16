package com.fersal.fersal.empresa.entity;


import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "empresas")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Empresa {


    @Id
    @GeneratedValue
    private UUID id;


    @Column(nullable = false)
    private String nombre;


    private String ruc;


    private String telefono;


    private String email;


    private String direccion;


    private String sitioWeb;


    private Boolean activo;


    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;


    // NUEVO
    @Column(name = "logo_url")
    private String logoUrl;



    @PrePersist
    public void prePersist() {

        createdAt = LocalDateTime.now();

        activo = true;

    }


    @PreUpdate
    public void preUpdate() {

        updatedAt = LocalDateTime.now();

    }

}