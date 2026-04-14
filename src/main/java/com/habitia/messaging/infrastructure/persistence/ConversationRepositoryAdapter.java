package com.habitia.messaging.infrastructure.persistence;

import com.habitia.messaging.domain.Conversation;
import com.habitia.messaging.domain.ConversationRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ConversationRepositoryAdapter implements ConversationRepository {

    private final ConversationJpaRepository jpaRepository;

    public ConversationRepositoryAdapter(ConversationJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Conversation save(Conversation conversation) {
        return toDomain(jpaRepository.save(toEntity(conversation)));
    }

    @Override
    public Optional<Conversation> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Conversation> findByBookingId(UUID bookingId) {
        return jpaRepository.findByBookingId(bookingId).map(this::toDomain);
    }

    @Override
    public boolean existsByBookingId(UUID bookingId) {
        return jpaRepository.existsByBookingId(bookingId);
    }

    private ConversationJpaEntity toEntity(Conversation c) {
        ConversationJpaEntity e = new ConversationJpaEntity();
        e.setId(c.getId());
        e.setBookingId(c.getBookingId());
        e.setGuestId(c.getGuestId());
        e.setHostId(c.getHostId());
        e.setCreatedAt(c.getCreatedAt());
        return e;
    }

    private Conversation toDomain(ConversationJpaEntity e) {
        return new Conversation(e.getId(), e.getBookingId(), e.getGuestId(), e.getHostId(), e.getCreatedAt());
    }
}
