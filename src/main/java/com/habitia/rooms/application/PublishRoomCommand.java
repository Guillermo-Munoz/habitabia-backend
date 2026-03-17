package com.habitia.rooms.application;

import java.math.BigDecimal;

public record PublishRoomCommand(
        String hostId,
        String title,
        String description,
        String street,
        String city,
        String country,
        Double latitude,
        Double longitude,
        BigDecimal priceAmount,
        String priceCurrency,
        int maxGuests
) {}