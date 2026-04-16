package com.habitia.notifications.application;

import com.habitia.notifications.domain.Notification;
import com.habitia.notifications.domain.NotificationRepository;
import com.habitia.notifications.domain.NotificationType;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateNotificationUseCase {

    private final NotificationRepository notificationRepository;

    public CreateNotificationUseCase(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification execute(UUID recipientId, NotificationType type, String message, UUID referenceId) {
        return notificationRepository.save(new Notification(recipientId, type, message, referenceId));
    }
}
