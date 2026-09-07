package com.fersal.fersal.caja.movimiento.repository;


import com.fersal.fersal.caja.movimiento.entity.MovimientoCaja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.UUID;


public interface MovimientoCajaRepository
        extends JpaRepository<MovimientoCaja, UUID> {


    @Query("""
            SELECT COALESCE(SUM(m.monto), 0)
            FROM MovimientoCaja m
            WHERE m.sucursal.id = :sucursalId
            AND m.tipo = :tipo
            """)
    BigDecimal sumBySucursalIdAndTipo(
            @Param("sucursalId") UUID sucursalId,
            @Param("tipo") String tipo
    );

}