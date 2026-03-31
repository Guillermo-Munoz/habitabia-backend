package com.habitia.shared.infrastructure.storage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.habitia.shared.domain.storage.StorageService;

import jakarta.annotation.PostConstruct;

@Service
public class LocalStorageService implements StorageService {

    private static final List<byte[]> ALLOWED_MAGIC_BYTES = List.of(
        new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF},             // JPG
        new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A}          // PNG
    );

    private final Path rootLocation;
    private final String baseUrl;

    public LocalStorageService(StorageProperties props) {
        this.rootLocation = Paths.get(props.uploadDir()).toAbsolutePath().normalize();
        this.baseUrl = props.baseUrl();
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo inicializar el almacenamiento", e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("No se puede almacenar un archivo vacío.");
        }

        // Validación MIME básica (extra, no sustituye magic bytes)
        if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
            throw new RuntimeException("Tipo MIME no válido");
        }

        byte[] firstBytes = readFirstBytes(file, 8);
        String extension = detectExtension(firstBytes);
        String filename = UUID.randomUUID() + extension;

        try (InputStream inputStream = file.getInputStream()) {
            Path destination = rootLocation.resolve(filename).normalize();

            // Seguridad: evitar path traversal
            if (!destination.startsWith(rootLocation)) {
                throw new RuntimeException("Ruta no permitida");
            }

            Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            throw new RuntimeException("Error al almacenar el archivo", e);
        }

        return baseUrl + "/uploads/" + filename;
    }

    @Override
    public void delete(String fileUrl) {
        String filename = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);

        try {
            Path filePath = rootLocation.resolve(filename).normalize();

            // Seguridad: evitar borrar fuera del directorio
            if (!filePath.startsWith(rootLocation)) {
                throw new RuntimeException("Intento de acceso no permitido");
            }

            Files.deleteIfExists(filePath);

        } catch (IOException e) {
            throw new RuntimeException("Error al borrar el archivo", e);
        }
    }

    // -------------------------
    // Helpers
    // -------------------------

    private byte[] readFirstBytes(MultipartFile file, int length) {
        try (InputStream is = file.getInputStream()) {
            byte[] buffer = new byte[length];
            int read = is.read(buffer);
            return read > 0 ? buffer : new byte[0];
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo", e);
        }
    }

    private String detectExtension(byte[] bytes) {
        if (startsWith(bytes, ALLOWED_MAGIC_BYTES.get(0))) return ".jpg";
        if (startsWith(bytes, ALLOWED_MAGIC_BYTES.get(1))) return ".png";
        throw new RuntimeException("Tipo de archivo no permitido. Solo JPG y PNG");
    }

    private boolean startsWith(byte[] data, byte[] magic) {
        if (data.length < magic.length) return false;
        for (int i = 0; i < magic.length; i++) {
            if (data[i] != magic[i]) return false;
        }
        return true;
    }
}