package com.habitia.moderation.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class BannedWord {
    private final UUID id;
    private final String word;
    private final LocalDateTime createdAt;

    /** Cear una nueva palabra prohibida. */
    public BannedWord(String word){
        if(word == null || word.isBlank()) throw new IllegalArgumentException("Word cannot be null or blank");
        this.id = UUID.randomUUID();
        this.word = word.toLowerCase();
        this.createdAt = LocalDateTime.now();
    }
    /** Reconstruye desde la base de datos palabras prohibidas. */
    public BannedWord(UUID id, String word, LocalDateTime createdAt){
        this.id = id;
        this.word = word.toLowerCase();
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }
    public String getWord() {
        return word;
    }  
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
