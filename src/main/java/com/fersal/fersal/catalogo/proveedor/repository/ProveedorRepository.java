package com.fersal.fersal.catalogo.proveedor.repository;


import com.fersal.fersal.catalogo.proveedor.entity.Proveedor;

import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.UUID;


public interface ProveedorRepository
        extends JpaRepository<Proveedor, UUID> {


    List<Proveedor> findByEmpresaId(UUID empresaId);


}