package com.fersal.fersal.storage.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

import java.util.UUID;

@Service
public class FileStorageService {

    private final Path uploadDirectory;

    public FileStorageService(
            @Value("${app.upload.dir:uploads}") String uploadDir
    ) {

        this.uploadDirectory =
                Paths.get(uploadDir)
                        .toAbsolutePath()
                        .normalize();

        try {

            Files.createDirectories(uploadDirectory);

        } catch (IOException e) {

            throw new RuntimeException(
                    "No se pudo crear el directorio de uploads",
                    e
            );
        }
    }

    public String guardarImagenProducto(
            UUID empresaId,
            UUID productoId,
            MultipartFile archivo
    ) {

        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException(
                    "La imagen está vacía"
            );
        }

        String contentType = archivo.getContentType();

        if (contentType == null ||
                !contentType.startsWith("image/")) {

            throw new IllegalArgumentException(
                    "El archivo debe ser una imagen"
            );
        }

        Path directorioProducto =
                uploadDirectory
                        .resolve("empresas")
                        .resolve(empresaId.toString())
                        .resolve("productos")
                        .resolve(productoId.toString());

        try {

            Files.createDirectories(directorioProducto);

            eliminarImagenesExistentes(
                    directorioProducto
            );

            String extension =
                    obtenerExtension(archivo);

            Path destino =
                    directorioProducto.resolve(
                            "imagen" + extension
                    );

            try (InputStream inputStream =
                         archivo.getInputStream()) {

                Files.copy(
                        inputStream,
                        destino,
                        StandardCopyOption.REPLACE_EXISTING
                );
            }

            return "/uploads/empresas/"
                    + empresaId
                    + "/productos/"
                    + productoId
                    + "/"
                    + destino.getFileName();

        } catch (IOException e) {

            throw new RuntimeException(
                    "No se pudo guardar la imagen",
                    e
            );
        }
    }

    public void eliminarImagenProducto(
            UUID empresaId,
            UUID productoId
    ) {

        Path directorioProducto =
                uploadDirectory
                        .resolve("empresas")
                        .resolve(empresaId.toString())
                        .resolve("productos")
                        .resolve(productoId.toString());

        if (!Files.exists(directorioProducto)) {
            return;
        }

        try {

            eliminarImagenesExistentes(
                    directorioProducto
            );

            Files.deleteIfExists(
                    directorioProducto
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "No se pudo eliminar la imagen",
                    e
            );
        }
    }

    private void eliminarImagenesExistentes(
            Path directorio
    ) throws IOException {

        try (DirectoryStream<Path> archivos =
                     Files.newDirectoryStream(directorio)) {

            for (Path archivo : archivos) {

                if (Files.isRegularFile(archivo)) {

                    Files.deleteIfExists(archivo);
                }
            }
        }
    }

    private String obtenerExtension(
            MultipartFile archivo
    ) {

        String contentType =
                archivo.getContentType();

        if ("image/png".equals(contentType)) {
            return ".png";
        }

        if ("image/webp".equals(contentType)) {
            return ".webp";
        }

        if ("image/gif".equals(contentType)) {
            return ".gif";
        }

        return ".jpg";
    }
}