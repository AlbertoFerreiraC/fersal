package com.fersal.fersal.cliente.pago.repository;


import com.fersal.fersal.cliente.pago.entity.PagoCliente;


import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.UUID;


public interface PagoClienteRepository
        extends JpaRepository<PagoCliente, UUID> {


    List<PagoCliente> findByClienteId(
            UUID clienteId
    );


    List<PagoCliente> findByCuentaCorrienteId(
            UUID cuentaId
    );


}