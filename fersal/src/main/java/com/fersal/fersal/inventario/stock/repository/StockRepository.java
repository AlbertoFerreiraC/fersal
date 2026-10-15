package com.fersal.fersal.inventario.stock.repository;

import com.fersal.fersal.inventario.stock.entity.Stock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StockRepository extends JpaRepository<Stock, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Stock s WHERE s.sucursal.id = :sucursalId AND s.producto.id = :productoId")
    Optional<Stock> findBySucursalIdAndProductoIdForUpdate(
            @Param("sucursalId") UUID sucursalId,
            @Param("productoId") UUID productoId
    );

    Optional<Stock> findBySucursalIdAndProductoId(
            UUID sucursalId,
            UUID productoId
    );

    List<Stock> findBySucursalId(UUID sucursalId);

    List<Stock> findByEmpresaId(UUID empresaId);

    List<Stock> findByEmpresaIdAndSucursalId(UUID empresaId, UUID sucursalId);

    List<Stock> findByCantidadLessThanEqual(Integer stockMinimo);

    @Query("""
            SELECT s
            FROM Stock s
            WHERE s.empresa.id = :empresaId
            AND s.sucursal.id = :sucursalId
            AND s.cantidad <= s.stockMinimo
            ORDER BY s.cantidad ASC
            """)
    List<Stock> findStockBajo(
            @Param("empresaId") UUID empresaId,
            @Param("sucursalId") UUID sucursalId,
            org.springframework.data.domain.Pageable pageable
    );

    @Query("""
            SELECT s
            FROM Stock s
            WHERE s.empresa.id = :empresaId
            AND s.cantidad <= s.stockMinimo
            ORDER BY s.cantidad ASC
            """)
    List<Stock> findStockBajoByEmpresa(
            @Param("empresaId") UUID empresaId
    );
}