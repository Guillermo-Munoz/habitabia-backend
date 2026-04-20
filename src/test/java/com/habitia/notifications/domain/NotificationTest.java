package com.habitia.notifications.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class NotificationTest {

    private UUID recipientId;
    private UUID referenceId;

    @BeforeEach
    void setUp() {
        recipientId = UUID.randomUUID();
        referenceId = UUID.randomUUID();
    }

    // -------------------------------------------------------------------------
    // Creation rules
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("Creation rules")
    class CreationRules {

        @Test
        @DisplayName("should initialize with correct default state")
        void shouldInitializeWithCorrectDefaultState() {
            Notification notification = new Notification(
                    recipientId, NotificationType.REVIEW_RECEIVED, "You have a new review", referenceId);

            assertAll(
                    () -> assertNotNull(notification.getId()),
                    () -> assertFalse(notification.isRead()),
                    () -> assertNotNull(notification.getCreatedAt()),
                    () -> assertEquals(recipientId, notification.getRecipientId()),
                    () -> assertEquals(NotificationType.REVIEW_RECEIVED, notification.getType())
            );
        }

        @Test
        @DisplayName("should throw exception when recipientId is null")
        void shouldThrowException_whenRecipientIdIsNull() {
            assertThrows(IllegalArgumentException.class,
                    () -> new Notification(null, NotificationType.MESSAGE_RECEIVED, "Approved", referenceId));
        }

        @Test
        @DisplayName("should throw exception when type is null")
        void shouldThrowException_whenTypeIsNull() {
            assertThrows(IllegalArgumentException.class,
                    () -> new Notification(recipientId, null, "message", referenceId));
        }

        @Test
        @DisplayName("should throw exception when message is blank")
        void shouldThrowException_whenMessageIsBlank() {
            assertThrows(IllegalArgumentException.class,
                    () -> new Notification(recipientId, NotificationType.REVIEW_RECEIVED, "  ", referenceId));
        }

        @Test
        @DisplayName("should allow null referenceId")
        void shouldAllowNullReferenceId() {
            assertDoesNotThrow(() ->
                    new Notification(recipientId, NotificationType.REVIEW_RECEIVED, "message", null));
        }
    }

    // -------------------------------------------------------------------------
    // State transitions
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("State transitions")
    class StateTransitions {

        @Test
        @DisplayName("should mark notification as read")
        void shouldMarkNotificationAsRead() {
            Notification notification = new Notification(
                    recipientId, NotificationType.MESSAGE_RECEIVED, "New message", referenceId);

            notification.markAsRead();

            assertTrue(notification.isRead());
        }

        @Test
        @DisplayName("should remain read when markAsRead is called multiple times")
        void shouldRemainRead_whenMarkAsReadCalledMultipleTimes() {
            Notification notification = new Notification(
                    recipientId, NotificationType.MESSAGE_RECEIVED, "New message", referenceId);

            notification.markAsRead();
            notification.markAsRead();

            assertTrue(notification.isRead());
        }
    }

    // -------------------------------------------------------------------------
    // Full flow
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("Full flow")
    class FullFlow {

        @Test
        @DisplayName("should transition from unread to read correctly")
        void shouldTransitionFromUnreadToRead() {
            Notification notification = new Notification(
                    recipientId, NotificationType.MESSAGE_RECEIVED, "New message", referenceId);

            assertFalse(notification.isRead());

            notification.markAsRead();

            assertAll(
                    () -> assertTrue(notification.isRead()),
                    () -> assertEquals(recipientId, notification.getRecipientId()),
                    () -> assertNotNull(notification.getId())
            );
        }
    }
}
