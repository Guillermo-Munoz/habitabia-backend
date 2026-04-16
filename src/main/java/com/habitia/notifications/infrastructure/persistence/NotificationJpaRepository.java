package com.habitia.notifications.infrastructure.persistence;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationJpaRepository extends JpaRepository<NotificationJpaEntity, UUID>{
    Page<NotificationJpaEntity> findByRecipientId(UUID recipientId, Pageable pageable);
    
}
