package com.habitia.reviews.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository {
    Review save(Review review);
    Optional<Review> findById(UUID id);
    List<Review> findByBookingId(UUID id);
    List<Review> findByRoomId(UUID id);
    
}
