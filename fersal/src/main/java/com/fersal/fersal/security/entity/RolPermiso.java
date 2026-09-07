package com.fersal.fersal.security.entity;


import jakarta.persistence.*;

import lombok.*;

import java.util.UUID;


@Entity
@Table(name = "rol_permiso")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolPermiso {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Rol rol;


    @ManyToOne
    @JoinColumn(name = "permiso_id")
    private Permiso permiso;

}