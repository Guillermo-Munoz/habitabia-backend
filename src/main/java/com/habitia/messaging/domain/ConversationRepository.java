package com.habitia.messaging.domain;

import java.util.Optional;
import java.util.UUID;

public interface ConversationRepository {
    Conversation save(Conversation conversation);
    Optional<Conversation> findById(UUID id);
    Optional<Conversation> findByBookingId(UUID bookingId);
    boolean existsByBookingId(UUID bookingId);
}
