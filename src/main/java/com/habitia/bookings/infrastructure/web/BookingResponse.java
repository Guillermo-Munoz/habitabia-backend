package com.habitia.bookings.infrastructure.web;

import com.habitia.bookings.domain.Booking;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record BookingResponse(
        UUID id,
        UUID roomId,
        String guestId,
        String hostId,
        LocalDate checkIn,
        LocalDate checkOut,
        int guests,
        String status,
        String message,
        LocalDateTime createdAt
) {
    public static BookingResponse from(Booking b) {
        return new BookingResponse(
                b.getId(),
                b.getRoomId(),
                b.getGuestId().toString(),
                b.getHostId().toString(),
                b.getDateRange().checkIn(),
                b.getDateRange().checkOut(),
                b.getGuests(),
                b.getStatus().name(),
                b.getMessage(),
                b.getCreatedAt()
        );
    }
}