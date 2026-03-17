package com.habitia.rooms.infrastructure.web;

import com.habitia.rooms.domain.Room;

import java.math.BigDecimal;
import java.util.UUID;

public record RoomResponse(
        UUID id,
        String hostId,
        String title,
        String description,
        String city,
        String country,
        BigDecimal priceAmount,
        String priceCurrency,
        int maxGuests,
        String status
) {
    public static RoomResponse from(Room room) {
        return new RoomResponse(
                room.getId(),
                room.getHostId().toString(),
                room.getTitle(),
                room.getDescription(),
                room.getCity(),
                room.getCountry(),
                room.getPrice().amount(),
                room.getPrice().currency(),
                room.getMaxGuests(),
                room.getStatus().name()
        );
    }
}