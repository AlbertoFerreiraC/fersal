package com.fersal.fersal.catalogo.producto.repository;

import com.fersal.fersal.catalogo.producto.entity.Producto;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductoRepository
        extends JpaRepository<Producto, UUID> {

    List<Producto> findByEmpresaId(UUID empresaId);

    List<Producto> findByCategoriaId(UUID categoriaId);

    boolean existsByEmpresaIdAndCodigo(
            UUID empresaId,
            String codigo
    );
}