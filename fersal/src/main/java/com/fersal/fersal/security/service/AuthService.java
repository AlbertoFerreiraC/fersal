package com.fersal.fersal.security.service;


import com.fersal.fersal.security.dto.LoginRequest;
import com.fersal.fersal.security.dto.LoginResponse;
import com.fersal.fersal.security.entity.Usuario;
import com.fersal.fersal.security.jwt.JwtService;
import com.fersal.fersal.security.repository.UsuarioRepository;


import lombok.RequiredArgsConstructor;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {


    private final UsuarioRepository usuarioRepository;


    private final AuthenticationManager authenticationManager;


    private final JwtService jwtService;


    public LoginResponse login(LoginRequest request) {


        authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(

                        request.getEmail(),

                        request.getPassword()

                )

        );


        Usuario usuario =
                usuarioRepository.findByEmail(
                                request.getEmail()
                        )
                        .orElseThrow();


        String token =
                jwtService.generarToken(
                        usuario.getEmail()
                );


        return LoginResponse.builder()

                .token(token)

                .email(usuario.getEmail())

                .name(usuario.getNombre())

                .empresaId(
                        usuario.getEmpresa().getId().toString()
                )

                .empresaNombre(
                        usuario.getEmpresa().getNombre()
                )

                .build();

    }


    public LoginResponse biometricLogin(String email) {


        Usuario usuario =
                usuarioRepository.findByEmail(email)
                        .orElseThrow();


        String token =
                jwtService.generarToken(
                        usuario.getEmail()
                );


        return LoginResponse.builder()

                .token(token)

                .email(usuario.getEmail())

                .name(usuario.getNombre())

                .empresaId(
                        usuario.getEmpresa().getId().toString()
                )

                .empresaNombre(
                        usuario.getEmpresa().getNombre()
                )

                .build();

    }


}