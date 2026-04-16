package com.habitia.notifications.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Notification {

    private final UUID id;
    private final UUID recipientId;
    private final NotificationType type;
    private final String message;
    private final UUID referenceId; // ID del recurso relacionado (reseña, mensaje, reserva)
    private boolean isRead;
    private final LocalDateTime createdAt;

    public Notification(UUID recipientId, NotificationType type, String message, UUID referenceId) {
        if (recipientId == null) throw new IllegalArgumentException("Recipient ID cannot be null");
        if (type == null) throw new IllegalArgumentException("Type cannot be null");
        if (message == null || message.isBlank()) throw new IllegalArgumentException("Message cannot be empty");
        this.id = UUID.randomUUID();
        this.recipientId = recipientId;
        this.type = type;
        this.message = message;
        this.referenceId = referenceId;
        this.isRead = false;
        this.createdAt = LocalDateTime.now();
    }

    /** Reconstruye desde persistencia. */
    public Notification(UUID id, UUID recipientId, NotificationType type, String message,
                        UUID referenceId, boolean isRead, LocalDateTime createdAt) {
        this.id = id;
        this.recipientId = recipientId;
        this.type = type;
        this.message = message;
        this.referenceId = referenceId;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public void markAsRead() {
        this.isRead = true;
    }

    public UUID getId() { return id; }
    public UUID getRecipientId() { return recipientId; }
    public NotificationType getType() { return type; }
    public String getMessage() { return message; }
    public UUID getReferenceId() { return referenceId; }
    public boolean isRead() { return isRead; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
