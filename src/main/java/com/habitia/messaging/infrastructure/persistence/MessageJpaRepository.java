package com.habitia.messaging.infrastructure.persistence;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageJpaRepository extends JpaRepository<MessageJpaEntity, UUID> {
    Page<MessageJpaEntity> findByConversationId(UUID conversationId, Pageable pageable);
}
