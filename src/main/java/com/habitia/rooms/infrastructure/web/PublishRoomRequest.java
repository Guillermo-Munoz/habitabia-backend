package com.habitia.rooms.infrastructure.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PublishRoomRequest(
        @NotBlank String title,
        String description,
        String street,
        @NotBlank String city,
        @NotBlank String country,
        Double latitude,
        Double longitude,
        @NotNull @Positive BigDecimal priceAmount,
        @NotBlank String priceCurrency,
        @Positive int maxGuests
) {}