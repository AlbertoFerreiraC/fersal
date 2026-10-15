package com.fersal.fersal.compra.compra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface CuentaPorPagarRepository extends JpaRepository<com.fersal.fersal.compra.cuenta.entity.CuentaPorPagarDetalle, UUID> {

    List<com.fersal.fersal.compra.cuenta.entity.CuentaPorPagarDetalle> findByEmpresaIdAndActivoTrueOrderByFechaEmisionDesc(UUID empresaId);

    List<com.fersal.fersal.compra.cuenta.entity.CuentaPorPagarDetalle> findByProveedorIdAndActivoTrue(UUID proveedorId);

    @Query("""
            SELECT COALESCE(SUM(c.saldoPendiente), 0)
            FROM CuentaPorPagar c
            WHERE c.empresa.id = :empresaId
            AND c.estado = 'PENDIENTE'
            AND c.activo = true
            """)
    BigDecimal sumSaldoPendienteEmpresa(@Param("empresaId") UUID empresaId);
}