package com.fersal.fersal.venta.repository;


import com.fersal.fersal.venta.entity.DetalleVenta;

import org.springframework.data.jpa.repository.JpaRepository;


import java.util.UUID;


public interface DetalleVentaRepository
        extends JpaRepository<DetalleVenta, UUID> {


}