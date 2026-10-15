package com.fersal.fersal.storage.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/uploads")
public class FileController {

    private final Path uploadDirectory;

    public FileController(
            @Value("${app.upload.dir:uploads}") String uploadDir
    ) {

        this.uploadDirectory =
                Paths.get(uploadDir)
                        .toAbsolutePath()
                        .normalize();
    }

    @GetMapping("/empresas/{empresaId}/productos/{productoId}/{archivo}")
    public ResponseEntity<Resource> obtenerImagenProducto(
            @PathVariable String empresaId,
            @PathVariable String productoId,
            @PathVariable String archivo
    ) {

        try {

            Path archivoPath =
                    uploadDirectory
                            .resolve("empresas")
                            .resolve(empresaId)
                            .resolve("productos")
                            .resolve(productoId)
                            .resolve(archivo)
                            .normalize();

            if (!archivoPath.startsWith(uploadDirectory)) {
                return ResponseEntity.badRequest().build();
            }

            Resource resource =
                    new UrlResource(
                            archivoPath.toUri()
                    );

            if (!resource.exists() ||
                    !resource.isReadable()) {

                return ResponseEntity.notFound().build();
            }

            String contentType =
                    determinarContentType(archivo);

            return ResponseEntity.ok()
                    .contentType(
                            MediaType.parseMediaType(
                                    contentType
                            )
                    )
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "inline"
                    )
                    .body(resource);

        } catch (MalformedURLException e) {

            return ResponseEntity
                    .internalServerError()
                    .build();
        }
    }

    private String determinarContentType(
            String archivo
    ) {

        String nombre =
                archivo.toLowerCase();

        if (nombre.endsWith(".png")) {
            return "image/png";
        }

        if (nombre.endsWith(".webp")) {
            return "image/webp";
        }

        if (nombre.endsWith(".gif")) {
            return "image/gif";
        }

        return "image/jpeg";
    }
}