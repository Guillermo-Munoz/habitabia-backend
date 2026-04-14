package com.habitia.messaging.infrastructure.web;

import com.habitia.messaging.domain.Message;

import java.time.LocalDateTime;
import java.util.UUID;

public record MessageResponse(
        UUID id,
        UUID senderId,
        String content,
        LocalDateTime sentAt,
        boolean isRead
) {
    public static MessageResponse from(Message m) {
        return new MessageResponse(m.getId(), m.getSenderId(), m.getContent(), m.getSentAt(), m.isRead());
    }
}
