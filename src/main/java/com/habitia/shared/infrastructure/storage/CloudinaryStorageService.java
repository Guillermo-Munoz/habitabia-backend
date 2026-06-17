package com.habitia.shared.infrastructure.storage;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.habitia.shared.domain.storage.StorageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@ConditionalOnProperty(prefix = "app.storage", name = "provider", havingValue = "cloudinary")
@Service
public class CloudinaryStorageService implements StorageService {

    private final Cloudinary cloudinary;

    public CloudinaryStorageService(CloudinaryProperties props) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", props.cloudName(),
                "api_key", props.apiKey(),
                "api_secret", props.apiSecret()
        ));
    }

    @Override
    public String store(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Error al subir el archivo a Cloudinary", e);
        }
    }

    @Override
    public void delete(String fileUrl) {
        try {
            String publicId = extractPublicId(fileUrl);
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Error al borrar el archivo en Cloudinary", e);
        }
    }

    private String extractPublicId(String fileUrl) {
        String afterUpload = fileUrl.substring(fileUrl.indexOf("/upload/") + 8);
        if (afterUpload.startsWith("v") && afterUpload.indexOf('/') > 0) {
            afterUpload = afterUpload.substring(afterUpload.indexOf('/') + 1);
        }
        int dotIndex = afterUpload.lastIndexOf('.');
        return dotIndex > 0 ? afterUpload.substring(0, dotIndex) : afterUpload;
    }
}
