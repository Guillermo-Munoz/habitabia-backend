package com.habitia.messaging.infrastructure.persistence;

import com.habitia.messaging.domain.Message;
import com.habitia.messaging.domain.MessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MessageRepositoryAdapter implements MessageRepository {

    private final MessageJpaRepository jpaRepository;

    public MessageRepositoryAdapter(MessageJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Message save(Message message) {
        return toDomain(jpaRepository.save(toEntity(message)));
    }

    @Override
    public Page<Message> findByConversationId(UUID conversationId, Pageable pageable) {
        return jpaRepository.findByConversationId(conversationId, pageable).map(this::toDomain);
    }

    private MessageJpaEntity toEntity(Message m) {
        MessageJpaEntity e = new MessageJpaEntity();
        e.setId(m.getId());
        e.setConversationId(m.getConversationId());
        e.setSenderId(m.getSenderId());
        e.setContent(m.getContent());
        e.setSentAt(m.getSentAt());
        e.setRead(m.isRead());
        return e;
    }

    private Message toDomain(MessageJpaEntity e) {
        return new Message(e.getId(), e.getConversationId(), e.getSenderId(),
                e.getContent(), e.getSentAt(), e.isRead());
    }
}
