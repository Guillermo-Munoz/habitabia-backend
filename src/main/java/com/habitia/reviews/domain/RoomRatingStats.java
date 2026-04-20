package com.habitia.reviews.domain;

public record RoomRatingStats(
    double averageRating, 
    long totalReview
) {}
