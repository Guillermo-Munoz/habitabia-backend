package com.habitia.notifications.domain;

import java.util.Optional;
import java.util.UUID;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationRepository {
    Notification save(Notification totification);
    Optional<Notification>findById(UUID id);
    Page<Notification> findByRecipientId(UUID recipientId, Pageable pageable);
    
}
