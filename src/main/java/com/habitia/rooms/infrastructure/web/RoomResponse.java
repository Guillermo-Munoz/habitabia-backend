package com.habitia.rooms.infrastructure.web;

import com.habitia.rooms.domain.Amenity;
import com.habitia.rooms.domain.Room;
import com.habitia.reviews.domain.RoomRatingStats;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
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
        Set<Amenity> amenities,
        String status,
        List<String> imageUrls,
        double averageRating,
        long totalReviews
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
                room.getAmenities(),
                room.getStatus().name(),
                room.getImageUrls(),
                0.0,
                0L
        );
    }

    public static RoomResponse from(Room room, RoomRatingStats stats) {
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
                room.getAmenities(),
                room.getStatus().name(),
                room.getImageUrls(),
                stats.averageRating(),
                stats.totalReview()
        );
    }
}
