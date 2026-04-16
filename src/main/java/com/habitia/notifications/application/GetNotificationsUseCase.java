package com.habitia.notifications.application;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.habitia.notifications.domain.Notification;
import com.habitia.notifications.domain.NotificationRepository;

@Service
public class GetNotificationsUseCase {
    private final NotificationRepository notificationRepository;
    public GetNotificationsUseCase(NotificationRepository notificationRepository){
        this.notificationRepository = notificationRepository;
    }
    public Page<Notification> execute(UUID recipientId, Pageable pageable){
        return notificationRepository.findByRecipientId(recipientId, pageable);
    }
}
