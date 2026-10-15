package com.fersal.fersal.security.service;

import com.fersal.fersal.security.dto.UsuarioRequest;
import com.fersal.fersal.security.dto.UsuarioResponse;

import java.util.List;
import java.util.UUID;

public interface UsuarioService {
    UsuarioResponse crear(UsuarioRequest request);

    List<UsuarioResponse> listar();

    UsuarioResponse buscar(UUID id);

    void eliminar(UUID id);

    void actualizarFcmToken(UUID id, String token);
}