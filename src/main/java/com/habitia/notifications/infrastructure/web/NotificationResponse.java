package com.habitia.notifications.infrastructure.web;

import com.habitia.notifications.domain.Notification;
import com.habitia.notifications.domain.NotificationType;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationResponse(
        UUID id,
        NotificationType type,
        String message,
        UUID referenceId,
        boolean isRead,
        LocalDateTime createdAt
) {
    public static NotificationResponse from(Notification n) {
        return new NotificationResponse(n.getId(), n.getType(), n.getMessage(),
                n.getReferenceId(), n.isRead(), n.getCreatedAt());
    }
}
