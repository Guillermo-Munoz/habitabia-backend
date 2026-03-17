package com.habitia.bookings.infrastructure.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record BookingRequest(
        @NotBlank String roomId,
        @NotBlank String hostId,
        @NotNull LocalDate checkIn,
        @NotNull LocalDate checkOut,
        @Positive int guests,
        String message
) {}