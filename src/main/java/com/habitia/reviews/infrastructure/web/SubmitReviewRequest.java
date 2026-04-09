package com.habitia.reviews.infrastructure.web;

import java.util.UUID;

public record SubmitReviewRequest(
    UUID bookingId,
    int rating,
    String comment,
    boolean isHostReview,
    boolean isPublic
) {}
