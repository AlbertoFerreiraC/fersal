package com.fersal.fersal.sucursal.repository;


import com.fersal.fersal.sucursal.entity.Sucursal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface SucursalRepository
        extends JpaRepository<Sucursal, UUID> {


    List<Sucursal> findByEmpresaId(UUID empresaId);


}