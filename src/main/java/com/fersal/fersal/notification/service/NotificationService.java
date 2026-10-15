package com.fersal.fersal.notification.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @Async
    public void enviarPush(String token, String titulo, String cuerpo) {
        enviarPushConImagen(token, titulo, cuerpo, null);
    }

    @Async
    public void enviarPushConImagen(String token, String titulo, String cuerpo, String imagenUrl) {
        if (token == null || token.isBlank()) {
            log.warn("El token FCM es nulo o vacío.");
            return;
        }

        if (FirebaseApp.getApps().isEmpty()) {
            log.error("FirebaseApp NO está inicializado.");
            return;
        }

        String directImageUrl = convertirEnlaceDriveADirecto(imagenUrl);

        try {
            Message.Builder messageBuilder = Message.builder()
                    .setToken(token)
                    .putData("title", titulo != null ? titulo : "")
                    .putData("body", cuerpo != null ? cuerpo : "");

            if (directImageUrl != null && !directImageUrl.isBlank()) {
                messageBuilder.putData("image", directImageUrl);
            }

            String response = FirebaseMessaging.getInstance().send(messageBuilder.build());
            log.info("Push FCM enviada con éxito (Data-only). ID: {}", response);
        } catch (Exception e) {
            log.error("Error enviando notificación push FCM: ", e);
        }
    }

    private String convertirEnlaceDriveADirecto(String url) {
        if (url == null || url.isBlank()) return null;

        if (url.contains("drive.google.com")) {
            Pattern pattern = Pattern.compile("/d/([a-zA-Z0-9_-]+)");
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                String fileId = matcher.group(1);
                return "https://lh3.googleusercontent.com/d/" + fileId;
            }

            Pattern idPattern = Pattern.compile("id=([a-zA-Z0-9_-]+)");
            Matcher idMatcher = idPattern.matcher(url);
            if (idMatcher.find()) {
                String fileId = idMatcher.group(1);
                return "https://lh3.googleusercontent.com/d/" + fileId;
            }
        }
        return url;
    }
}