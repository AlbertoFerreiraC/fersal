package com.fersal.fersal.caja.cierre.repository;

import com.fersal.fersal.caja.cierre.entity.CierreCaja;
import com.fersal.fersal.caja.cierre.entity.EstadoCaja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CierreCajaRepository extends JpaRepository<CierreCaja, UUID> {

    @Query("SELECT c FROM CierreCaja c WHERE c.empresa.id = :empresaId AND c.sucursal.id = :sucursalId AND c.usuario.id = :usuarioId AND c.estado = :estado")
    Optional<CierreCaja> findByEmpresaIdAndSucursalIdAndUsuarioIdAndEstado(
            @Param("empresaId") UUID empresaId,
            @Param("sucursalId") UUID sucursalId,
            @Param("usuarioId") UUID usuarioId,
            @Param("estado") EstadoCaja estado
    );

    boolean existsByEmpresaIdAndSucursalIdAndUsuarioIdAndEstado(
            UUID empresaId,
            UUID sucursalId,
            UUID usuarioId,
            EstadoCaja estado
    );

    List<CierreCaja> findAllByOrderByFechaDesc();

    List<CierreCaja> findByEmpresaIdOrderByFechaDesc(UUID empresaId);

    List<CierreCaja> findByEmpresaIdAndSucursalIdOrderByFechaDesc(UUID empresaId, UUID sucursalId);

    List<CierreCaja> findByEmpresaIdAndSucursalIdAndUsuarioIdOrderByFechaDesc(
            UUID empresaId,
            UUID sucursalId,
            UUID usuarioId
    );
}