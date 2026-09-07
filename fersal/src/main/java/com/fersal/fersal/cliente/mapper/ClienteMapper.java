package com.fersal.fersal.cliente.mapper;


import com.fersal.fersal.cliente.dto.ClienteResponse;
import com.fersal.fersal.cliente.entity.Cliente;


import org.springframework.stereotype.Component;


@Component
public class ClienteMapper {


    public ClienteResponse toResponse(
            Cliente cliente
    ) {

        return ClienteResponse.builder()

                .id(cliente.getId())

                .nombre(cliente.getNombre())

                .apellido(cliente.getApellido())

                .documento(cliente.getDocumento())

                .telefono(cliente.getTelefono())

                .email(cliente.getEmail())

                .direccion(cliente.getDireccion())

                .activo(cliente.getActivo())

                .build();

    }

}