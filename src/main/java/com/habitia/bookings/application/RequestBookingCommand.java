package com.habitia.bookings.application;

import java.time.LocalDate;

public record RequestBookingCommand(
        String guestId,
        String roomId,
        String hostId,
        LocalDate checkIn,
        LocalDate checkOut,
        int guests,
        String message
) {}