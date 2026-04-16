package com.habitia.notifications.infrastructure.web;

import com.habitia.notifications.application.GetNotificationsUseCase;
import com.habitia.notifications.application.MarkNotificationAsReadUseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final GetNotificationsUseCase getNotifications;
    private final MarkNotificationAsReadUseCase markAsRead;

    public NotificationController(GetNotificationsUseCase getNotifications,
                                  MarkNotificationAsReadUseCase markAsRead) {
        this.getNotifications = getNotifications;
        this.markAsRead = markAsRead;
    }

    @GetMapping
    public ResponseEntity<Page<NotificationResponse>> getAll(
            Authentication auth,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        UUID userId = UUID.fromString(auth.getName());
        return ResponseEntity.ok(getNotifications.execute(userId, pageable).map(NotificationResponse::from));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<NotificationResponse> markAsRead(@PathVariable UUID id) {
        return ResponseEntity.ok(NotificationResponse.from(markAsRead.execute(id)));
    }
}
