package com.fersal.fersal.security.service;


import com.fersal.fersal.empresa.entity.Empresa;
import com.fersal.fersal.empresa.repository.EmpresaRepository;


import com.fersal.fersal.security.dto.UsuarioRequest;
import com.fersal.fersal.security.dto.UsuarioResponse;


import com.fersal.fersal.security.entity.Rol;
import com.fersal.fersal.security.entity.Usuario;


import com.fersal.fersal.security.repository.RolRepository;
import com.fersal.fersal.security.repository.UsuarioRepository;


import com.fersal.fersal.exception.ResourceNotFoundException;


import lombok.RequiredArgsConstructor;


import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {


    private final UsuarioRepository usuarioRepository;

    private final EmpresaRepository empresaRepository;

    private final RolRepository rolRepository;

    private final PasswordEncoder passwordEncoder;


    @Override
    public UsuarioResponse crear(
            UsuarioRequest request
    ) {


        Empresa empresa =
                empresaRepository.findById(
                                request.getEmpresaId()
                        )
                        .orElseThrow();


        Rol rol =
                rolRepository.findById(
                                request.getRolId()
                        )
                        .orElseThrow();


        Usuario usuario =
                Usuario.builder()

                        .empresa(empresa)

                        .nombre(request.getNombre())

                        .apellido(request.getApellido())

                        .email(request.getEmail())

                        .passwordHash(
                                passwordEncoder.encode(
                                        request.getPassword()
                                )
                        )

                        .rol(rol)

                        .activo(true)

                        .build();


        usuarioRepository.save(usuario);


        return convertir(usuario);

    }


    @Override
    public List<UsuarioResponse> listar() {

        return usuarioRepository.findAll()
                .stream()
                .map(this::convertir)
                .toList();

    }


    @Override
    public UsuarioResponse buscar(UUID id) {


        Usuario usuario =
                usuarioRepository.findById(id)
                        .orElseThrow();


        return convertir(usuario);

    }


    @Override
    public void eliminar(UUID id) {


        Usuario usuario =
                usuarioRepository.findById(id)
                        .orElseThrow();


        usuario.setActivo(false);


        usuarioRepository.save(usuario);

    }


    private UsuarioResponse convertir(
            Usuario usuario
    ) {

        return UsuarioResponse.builder()

                .id(usuario.getId())

                .nombre(usuario.getNombre())

                .apellido(usuario.getApellido())

                .email(usuario.getEmail())

                .rol(
                        usuario.getRol()
                                .getNombre()
                )

                .activo(usuario.getActivo())

                .build();

    }


}