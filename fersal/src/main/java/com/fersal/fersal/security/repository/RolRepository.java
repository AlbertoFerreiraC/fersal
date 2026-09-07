package com.fersal.fersal.security.repository;


import com.fersal.fersal.security.entity.Rol;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface RolRepository
        extends JpaRepository<Rol, UUID> {

}