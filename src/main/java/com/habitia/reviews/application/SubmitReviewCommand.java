package com.habitia.reviews.application;

import java.util.UUID;

public record SubmitReviewCommand(
    UUID bookingId,
    UUID reviewerId,
    int rating,
    String comment,
    boolean isHostReview,
    boolean isPublic
) {}
   
