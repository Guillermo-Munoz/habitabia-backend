package com.habitia.shared.infrastructure.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.cloudinary")
public record CloudinaryProperties (
    String cloudName,
    String apiKey,
    String apiSecret
) {}
