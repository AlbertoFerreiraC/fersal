package com.fersal.fersal.security.service;


import com.fersal.fersal.security.entity.Usuario;
import com.fersal.fersal.security.repository.UsuarioRepository;


import lombok.RequiredArgsConstructor;


import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService
        implements UserDetailsService {


    private final UsuarioRepository usuarioRepository;


    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {


        Usuario usuario =
                usuarioRepository.findByEmail(email)

                        .orElseThrow(
                                () -> new UsernameNotFoundException(
                                        "Usuario no encontrado"
                                )
                        );


        return User.builder()

                .username(usuario.getEmail())

                .password(usuario.getPasswordHash())

                .roles(
                        usuario.getRol()
                                .getNombre()
                                .replace("ROLE_", "")
                )

                .build();

    }

}