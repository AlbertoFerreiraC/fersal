package com.fersal.fersal.security.repository;


import com.fersal.fersal.security.entity.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.Optional;
import java.util.UUID;


public interface UsuarioRepository
        extends JpaRepository<Usuario, UUID>{


    @Query("""
            SELECT u 
            FROM Usuario u
            JOIN FETCH u.rol
            WHERE u.email = :email
            """)
    Optional<Usuario> findByEmail(String email);


}