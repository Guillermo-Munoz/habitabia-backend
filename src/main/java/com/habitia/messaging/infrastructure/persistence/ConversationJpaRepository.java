package com.habitia.messaging.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationJpaRepository extends JpaRepository<ConversationJpaEntity, UUID> {
    
    Optional<ConversationJpaEntity> findByBookingId(UUID booingId);
    boolean existsByBookingId(UUID bookingId);
}
