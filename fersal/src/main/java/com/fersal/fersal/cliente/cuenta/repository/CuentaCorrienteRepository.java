package com.fersal.fersal.cliente.cuenta.repository;


import com.fersal.fersal.cliente.cuenta.entity.CuentaCorriente;


import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;
import java.util.UUID;


public interface CuentaCorrienteRepository
        extends JpaRepository<CuentaCorriente, UUID> {


    Optional<CuentaCorriente> findByClienteId(
            UUID clienteId
    );


}