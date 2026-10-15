package com.fersal.fersal.compra.repository;

import com.fersal.fersal.compra.entity.Compra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CompraRepository extends JpaRepository<Compra, UUID> {
    List<Compra> findByEmpresaIdOrderByFechaDesc(UUID empresaId);

    List<Compra> findByEmpresaIdAndSucursalIdOrderByFechaDesc(UUID empresaId, UUID sucursalId);
}