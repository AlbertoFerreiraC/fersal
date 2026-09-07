package com.fersal.fersal.inventario.movimiento.repository;


import com.fersal.fersal.inventario.movimiento.entity.MovimientoStock;


import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.UUID;


public interface MovimientoStockRepository
        extends JpaRepository<MovimientoStock, UUID> {


    List<MovimientoStock> findByProductoId(
            UUID productoId
    );


}