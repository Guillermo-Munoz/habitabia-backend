package com.habitia.rooms.infrastructure.web;

import com.habitia.rooms.domain.Room;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record RoomResponse(
        UUID id,
        String hostId,
        String title,
        String description,
        String city,
        String country,
        Double latitude,
        Double longitude,
        BigDecimal priceAmount,
        String priceCurrency,
        int maxGuests,
        String status,
        List<String> imageUrls
) {
    public static RoomResponse from(Room room) {
        return new RoomResponse(
                room.getId(),
                room.getHostId().toString(),
                room.getTitle(),
                room.getDescription(),
                room.getCity(),
                room.getCountry(),
                room.getLatitude(),
                room.getLongitude(),
                room.getPrice().amount(),
                room.getPrice().currency(),
                room.getMaxGuests(),
                room.getStatus().name(),
                room.getImageUrls()
        );
    }
}