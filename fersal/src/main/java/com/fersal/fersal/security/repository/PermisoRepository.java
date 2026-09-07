package com.fersal.fersal.security.repository;


import com.fersal.fersal.security.entity.Permiso;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface PermisoRepository
        extends JpaRepository<Permiso, UUID> {


}