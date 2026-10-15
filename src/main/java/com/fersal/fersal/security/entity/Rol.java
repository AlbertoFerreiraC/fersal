package com.fersal.fersal.security.entity;


import jakarta.persistence.*;

import lombok.*;

import java.util.UUID;


@Entity
@Table(name = "roles")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rol {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    private String nombre;


    private String descripcion;


    private Boolean activo = true;


}