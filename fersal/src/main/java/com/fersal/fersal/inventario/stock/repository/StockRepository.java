package com.fersal.fersal.inventario.stock.repository;


import com.fersal.fersal.inventario.stock.entity.Stock;


import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface StockRepository
        extends JpaRepository<Stock, UUID> {


    Optional<Stock> findBySucursalIdAndProductoId(
            UUID sucursalId,
            UUID productoId
    );


    List<Stock> findBySucursalId(
            UUID sucursalId
    );


}