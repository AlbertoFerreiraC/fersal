package com.fersal.fersal.venta.repository;


import com.fersal.fersal.venta.entity.Venta;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface VentaRepository
        extends JpaRepository<Venta, UUID> {


    long countByEmpresaId(UUID empresaId);


}