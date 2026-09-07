package com.fersal.fersal.security.entity;


import jakarta.persistence.*;

import lombok.*;

import java.util.UUID;


@Entity
@Table(name = "permisos")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permiso {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @Column(unique = true)
    private String nombre;


    private String descripcion;


}