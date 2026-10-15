package com.fersal.fersal.venta.repository;


import com.fersal.fersal.venta.entity.DetalleVenta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


public interface DetalleVentaRepository
        extends JpaRepository<DetalleVenta, UUID> {

    @Query("""
            SELECT COALESCE(SUM(d.cantidad * d.producto.precioCompra), 0)
            FROM DetalleVenta d
            WHERE d.venta.empresa.id = :empresaId
            AND d.venta.sucursal.id = :sucursalId
            AND d.venta.fecha BETWEEN :inicio AND :fin
            AND d.venta.estado != 'ANULADA'
            """)
    BigDecimal sumCostoProductosVendidosHoy(
            @Param("empresaId") UUID empresaId,
            @Param("sucursalId") UUID sucursalId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

}