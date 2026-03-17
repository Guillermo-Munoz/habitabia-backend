package com.habitia.shared.infrastructure.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.jwt")
public record JwtProperties(String secret, long expirationMs) {

    // Constructor compacto para validaciones (opcional)
    public JwtProperties {
        if (secret == null || secret.length() < 32) {
            throw new IllegalArgumentException("JWT secret debe tener al menos 32 caracteres");
        }
        if (expirationMs <= 0) {
            throw new IllegalArgumentException("expirationMs debe ser positivo");
        }
    }
}