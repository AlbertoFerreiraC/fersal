package com.fersal.fersal.venta.repository;

import com.fersal.fersal.venta.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface VentaRepository extends JpaRepository<Venta, UUID> {

    long countByEmpresaId(UUID empresaId);

    List<Venta> findAllByOrderByFechaDesc();

    List<Venta> findByEmpresaIdOrderByFechaDesc(UUID empresaId);

    List<Venta> findByEmpresaIdAndSucursalIdOrderByFechaDesc(UUID empresaId, UUID sucursalId);

    @Query("""
            SELECT COALESCE(SUM(v.total), 0)
            FROM Venta v
            WHERE v.empresa.id = :empresaId
            AND v.sucursal.id = :sucursalId
            AND v.fecha BETWEEN :inicio AND :fin
            AND v.estado != 'ANULADA'
            """)
    BigDecimal sumTotalVentasHoy(
            @Param("empresaId") UUID empresaId,
            @Param("sucursalId") UUID sucursalId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

    @Query("""
            SELECT COUNT(v)
            FROM Venta v
            WHERE v.empresa.id = :empresaId
            AND v.sucursal.id = :sucursalId
            AND v.fecha BETWEEN :inicio AND :fin
            AND v.estado != 'ANULADA'
            """)
    Long countVentasHoy(
            @Param("empresaId") UUID empresaId,
            @Param("sucursalId") UUID sucursalId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

    List<Venta> findTop5ByEmpresaIdAndSucursalIdOrderByFechaDesc(UUID empresaId, UUID sucursalId);
}