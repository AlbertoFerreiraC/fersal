package com.fersal.fersal.security.service;


import com.fersal.fersal.security.dto.LoginRequest;
import com.fersal.fersal.security.dto.LoginResponse;
import com.fersal.fersal.security.entity.Usuario;
import com.fersal.fersal.security.jwt.JwtService;
import com.fersal.fersal.security.repository.UsuarioRepository;


import lombok.RequiredArgsConstructor;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {


    private final UsuarioRepository usuarioRepository;


    private final AuthenticationManager authenticationManager;


    private final JwtService jwtService;


    public LoginResponse login(LoginRequest request) {

        System.out.println(
                new BCryptPasswordEncoder()
                        .encode("123456")
        );

        authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(

                        request.getEmail(),

                        request.getPassword()

                )

        );


        Usuario usuario = usuarioRepository.findByEmail(request.getEmail()).orElseThrow();


        String token = jwtService.generarToken(usuario.getEmail());


        return construirRespuesta(usuario, token);

    }


    public LoginResponse biometricLogin(String email) {


        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow();


        String token = jwtService.generarToken(usuario.getEmail());


        return construirRespuesta(usuario, token);


    }


    private LoginResponse construirRespuesta(Usuario usuario, String token) {


        return LoginResponse.builder()


                .token(token)


                .usuarioId(usuario.getId().toString())


                .email(usuario.getEmail())


                .name(usuario.getNombre())


                .empresaId(usuario.getEmpresa().getId().toString())


                .empresaNombre(usuario.getEmpresa().getNombre())


                .sucursalId(usuario.getSucursal() != null ? usuario.getSucursal().getId().toString() : null)


                .sucursalNombre(usuario.getSucursal() != null ? usuario.getSucursal().getNombre() : null)


                .build();


    }


}