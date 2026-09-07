package com.fersal.fersal.caja.cierre.repository;


import com.fersal.fersal.caja.cierre.entity.CierreCaja;


import org.springframework.data.jpa.repository.JpaRepository;


import java.util.UUID;


public interface CierreCajaRepository
        extends JpaRepository<CierreCaja, UUID> {


}