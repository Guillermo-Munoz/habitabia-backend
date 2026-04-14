package com.habitia.messaging.domain;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MessageRepository {
    Message save(Message message);
    Page<Message> findByConversationId(UUID conversationId, Pageable pageable);
}