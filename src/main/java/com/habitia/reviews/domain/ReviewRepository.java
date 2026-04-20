package com.habitia.reviews.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewRepository {
    Review save(Review review);
    Optional<Review> findById(UUID id);
    List<Review> findByBookingId(UUID id);
    List<Review> findByRoomId(UUID id);
    boolean existsByBookingIdAndReviewerId(UUID bookingId, UUID reviewerId);
    List<Review> findFlaggedPendingReview();
    Page<Review> findByRoomId(UUID id, Pageable pageable);
    Page<Review> findFlaggedPendingReview(Pageable pageable);
    RoomRatingStats getRatingStatsByRoomId(UUID roomId);

}
