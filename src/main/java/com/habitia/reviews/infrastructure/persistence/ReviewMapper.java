package com.habitia.reviews.infrastructure.persistence;

import com.habitia.reviews.domain.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public ReviewJpaEntity toEntity(Review review) {
        ReviewJpaEntity e = new ReviewJpaEntity();
        e.setId(review.getId());
        e.setBookingId(review.getBookingId());
        e.setReviewerId(review.getReviewerId());
        e.setRating(review.getRating());
        e.setComment(review.getComment());
        e.setCreatedAt(review.getCreatedAt());
        e.setHostReview(review.isHostReview());
        e.setPublic(review.isPublic());
        e.setApproved(review.isApproved());
        e.setDeleted(review.isDeleted());
        e.setFlagged(review.isFlagged());
        e.setVerified(review.isVerified());
        e.setEdited(review.isEdited());
        e.setResponded(review.isResponded());
        e.setApprovedAt(review.getApprovedAt());
        e.setFlaggedAt(review.getFlaggedAt());
        e.setFlagReason(review.getFlagReason());
        e.setEditedAt(review.getEditedAt());
        e.setEditCount(review.getEditCount());
        e.setHostResponse(review.getHostResponse());
        e.setRespondedAt(review.getRespondedAt());
        e.setDeletedAt(review.getDeletedAt());
        return e;
    }

    public Review toDomain(ReviewJpaEntity e) {
        return new Review(
                e.getId(),
                e.getBookingId(),
                e.getReviewerId(),
                e.getRating(),
                e.getComment(),
                e.getCreatedAt(),
                e.isHostReview(),
                e.isPublic(),
                e.isApproved(),
                e.isDeleted(),
                e.isFlagged(),
                e.isVerified(),
                e.isEdited(),
                e.isResponded(),
                e.getApprovedAt(),
                e.getFlaggedAt(),
                e.getFlagReason(),
                e.getEditedAt(),
                e.getEditCount(),
                e.getHostResponse(),
                e.getRespondedAt(),
                e.getDeletedAt()
        );
    }
}
