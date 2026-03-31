package com.habitia.shared.infrastructure.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.storage")
public record StorageProperties(
    String uploadDir,
    String baseUrl
) {}  
    

