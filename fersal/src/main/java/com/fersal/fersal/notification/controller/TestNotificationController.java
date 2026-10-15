package com.fersal.fersal.notification.controller;

import com.fersal.fersal.notification.service.NotificationService;
import com.fersal.fersal.security.entity.Usuario;
import com.fersal.fersal.security.repository.UsuarioRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class TestNotificationController {

    private final UsuarioRepository usuarioRepository;
    private final NotificationService notificationService;

    @PostMapping("/test/{usuarioId}")
    public ResponseEntity<String> enviarPrueba(
            @PathVariable UUID usuarioId,
            @RequestBody NotificacionPruebaRequest request
    ) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (usuario.getFcmToken() == null || usuario.getFcmToken().isBlank()) {
            return ResponseEntity.badRequest().body("El usuario no tiene un fcmToken registrado");
        }

        notificationService.enviarPushConImagen(
                usuario.getFcmToken(),
                request.getTitulo(),
                request.getMensaje(),
                request.getImagenUrl()
        );

        return ResponseEntity.ok("Notificación con imagen enviada a: " + usuario.getEmail());
    }

    @Data
    public static class NotificacionPruebaRequest {
        private String titulo;
        private String mensaje;
        private String imagenUrl;
    }
}