package com.fersal.fersal.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    private static final Logger log = LoggerFactory.getLogger(FirebaseConfig.class);

    @PostConstruct
    public void init() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                ClassPathResource resource = new ClassPathResource("serviceAccountKey.json");

                if (!resource.exists()) {
                    log.error("ERROR CRÍTICO: No se encontró el archivo serviceAccountKey.json en src/main/resources/");
                    return;
                }

                try (InputStream is = resource.getInputStream()) {
                    FirebaseOptions options = FirebaseOptions.builder()
                            .setCredentials(GoogleCredentials.fromStream(is))
                            .build();

                    FirebaseApp.initializeApp(options);
                    log.info(">>> FirebaseApp inicializado correctamente con éxito <<<");
                }
            }
        } catch (Exception e) {
            log.error("Error al inicializar FirebaseApp: ", e);
        }
    }
}