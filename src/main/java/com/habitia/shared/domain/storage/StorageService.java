package com.habitia.shared.domain.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    
    /*Guarda archivo y devuelkve la URL pública */
    String store(MultipartFile file);

    /*Borrar archivo físico dad su URL */
    void delete(String fileUrl);


}
