package com.fersal.fersal.catalogo.proveedor.entity;


import com.fersal.fersal.empresa.entity.Empresa;

import jakarta.persistence.*;

import lombok.*;


import java.util.UUID;


@Entity
@Table(name = "proveedores")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Proveedor {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;


    private String nombre;


    private String ruc;


    private String telefono;


    private String email;


    private String direccion;


    private Boolean activo = true;


}