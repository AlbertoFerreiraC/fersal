package com.fersal.fersal.cliente.repository;


import com.fersal.fersal.cliente.entity.Cliente;

import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.UUID;


public interface ClienteRepository
        extends JpaRepository<Cliente, UUID> {


    List<Cliente> findByEmpresaId(UUID empresaId);


}