package com.fersal.fersal.cliente.cuenta.repository;

import com.fersal.fersal.cliente.cuenta.entity.CuentaCorriente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CuentaCorrienteRepository extends JpaRepository<CuentaCorriente, UUID> {

    Optional<CuentaCorriente> findByClienteId(UUID clienteId);

    List<CuentaCorriente> findByEmpresaIdAndActivoTrue(UUID empresaId);

    List<CuentaCorriente> findByEmpresaId(UUID empresaId);

    @Query("""
            SELECT COALESCE(SUM(c.saldo), 0)
            FROM CuentaCorriente c
            WHERE c.empresa.id = :empresaId
            AND c.activo = true
            """)
    BigDecimal sumSaldoPendiente(@Param("empresaId") UUID empresaId);
}