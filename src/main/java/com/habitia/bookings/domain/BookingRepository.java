package com.habitia.bookings.domain;

import com.habitia.shared.domain.valueobject.UserId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository {
    Booking save(Booking booking);
    Optional<Booking> findById(UUID id);
    List<Booking> findByGuestId(UserId guestId);
    List<Booking> findByHostId(UserId hostId);
    boolean existsOverlap(UUID roomId, LocalDate checkIn, LocalDate checkOut);
}