package com.habitia.bookings.domain;

import com.habitia.shared.domain.exception.BusinessRuleException;
import com.habitia.shared.domain.valueobject.DateRange;
import com.habitia.shared.domain.valueobject.UserId;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Booking {

    private final UUID id;
    private final UUID roomId;
    private final UserId guestId;
    private final UserId hostId;
    private final DateRange dateRange;
    private final int guests;
    private BookingStatus status;
    private final String message;
    private final LocalDateTime createdAt;

    // Constructor para nueva reserva
    public Booking(UUID roomId, UserId guestId, UserId hostId,
                   LocalDate checkIn, LocalDate checkOut,
                   int guests, String message) {
        this.id = UUID.randomUUID();
        this.roomId = roomId;
        this.guestId = guestId;
        this.hostId = hostId;
        this.dateRange = new DateRange(checkIn, checkOut);
        this.guests = guests;
        this.status = BookingStatus.REQUESTED;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }

    // Constructor para reconstruir desde persistencia
    public Booking(UUID id, UUID roomId, UserId guestId, UserId hostId,
                   LocalDate checkIn, LocalDate checkOut,
                   int guests, BookingStatus status,
                   String message, LocalDateTime createdAt) {
        this.id = id;
        this.roomId = roomId;
        this.guestId = guestId;
        this.hostId = hostId;
        this.dateRange = new DateRange(checkIn, checkOut);
        this.guests = guests;
        this.status = status;
        this.message = message;
        this.createdAt = createdAt;
    }

    public void accept() {
        if (status != BookingStatus.REQUESTED) {
            throw new BusinessRuleException("Only REQUESTED bookings can be accepted");
        }
        this.status = BookingStatus.ACCEPTED;
    }

    public void reject() {
        if (status != BookingStatus.REQUESTED) {
            throw new BusinessRuleException("Only REQUESTED bookings can be rejected");
        }
        this.status = BookingStatus.CANCELLED;
    }

    public void confirm() {
        if (status != BookingStatus.ACCEPTED) {
            throw new BusinessRuleException("Only ACCEPTED bookings can be confirmed");
        }
        this.status = BookingStatus.CONFIRMED;
    }

    public void cancel() {
        if (status == BookingStatus.COMPLETED || status == BookingStatus.CANCELLED) {
            throw new BusinessRuleException("Cannot cancel a COMPLETED or already CANCELLED booking");
        }
        this.status = BookingStatus.CANCELLED;
    }

    public void complete() {
        if (status != BookingStatus.CONFIRMED) {
            throw new BusinessRuleException("Only CONFIRMED bookings can be completed");
        }
        this.status = BookingStatus.COMPLETED;
    }
}