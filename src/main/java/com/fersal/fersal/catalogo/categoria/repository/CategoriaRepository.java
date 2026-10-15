package com.fersal.fersal.catalogo.categoria.repository;


import com.fersal.fersal.catalogo.categoria.entity.Categoria;

import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.UUID;


public interface CategoriaRepository
        extends JpaRepository<Categoria, UUID> {


    List<Categoria> findByEmpresaId(UUID empresaId);


}