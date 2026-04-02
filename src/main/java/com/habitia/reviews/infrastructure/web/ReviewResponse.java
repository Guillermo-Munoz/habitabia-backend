package com.habitia.reviews.infrastructure.web;

import com.habitia.reviews.domain.Review;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReviewResponse(
        UUID id,
        UUID reviewerId,
        int rating,
        String comment,
        LocalDateTime createdAt,
        boolean isEdited,
        String hostResponse,
        LocalDateTime respondedAt
) {
    public static ReviewResponse from(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getReviewerId(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt(),
                review.isEdited(),
                review.getHostResponse(),
                review.getRespondedAt()
        );
    }
}
