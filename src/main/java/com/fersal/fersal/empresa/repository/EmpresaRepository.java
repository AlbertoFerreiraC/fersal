package com.fersal.fersal.empresa.repository;


import com.fersal.fersal.empresa.entity.Empresa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface EmpresaRepository
        extends JpaRepository<Empresa, UUID> {


}