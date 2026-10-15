package com.fersal.fersal.compra.compra.repository;

import com.fersal.fersal.compra.cuenta.entity.CuentaPorPagar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface CuentaPorPagarRepository extends JpaRepository<CuentaPorPagar, UUID> {

    List<CuentaPorPagar> findByEmpresaIdAndActivoTrueOrderByFechaEmisionDesc(UUID empresaId);

    List<CuentaPorPagar> findByProveedorIdAndActivoTrue(UUID proveedorId);

    @Query("""
            SELECT COALESCE(SUM(c.saldoPendiente), 0)
            FROM CuentaPorPagar c
            WHERE c.empresa.id = :empresaId
            AND c.estado = 'PENDIENTE'
            AND c.activo = true
            """)
    BigDecimal sumSaldoPendienteEmpresa(@Param("empresaId") UUID empresaId);
}