package com.habitia.notifications.infrastructure.persistence;

import com.habitia.notifications.domain.Notification;
import com.habitia.notifications.domain.NotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
@Component
public class NotificationRepositoryAdapter implements NotificationRepository{
    private final NotificationJpaRepository jpaRepository;
    public NotificationRepositoryAdapter(NotificationJpaRepository jpaRepository){
        this.jpaRepository = jpaRepository;
    }
    @Override
    public Notification save(Notification notification){
        return toDomain(jpaRepository.save(toEntity(notification)));
    }
    
 @Override
    public Optional<Notification> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Page<Notification> findByRecipientId(UUID recipientId, Pageable pageable) {
        return jpaRepository.findByRecipientId(recipientId, pageable).map(this::toDomain);
    }

    private NotificationJpaEntity toEntity(Notification n) {
        NotificationJpaEntity e = new NotificationJpaEntity();
        e.setId(n.getId());
        e.setRecipientId(n.getRecipientId());
        e.setType(n.getType());
        e.setMessage(n.getMessage());
        e.setReferenceId(n.getReferenceId());
        e.setRead(n.isRead());
        e.setCreatedAt(n.getCreatedAt());
        return e;
    }

    private Notification toDomain(NotificationJpaEntity e) {
        return new Notification(e.getId(), e.getRecipientId(), e.getType(),
                e.getMessage(), e.getReferenceId(), e.isRead(), e.getCreatedAt());
    }
}