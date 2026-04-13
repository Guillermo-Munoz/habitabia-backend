package com.habitia.reviews.infrastructure.persistence;

import com.habitia.reviews.domain.Review;
import com.habitia.reviews.domain.ReviewRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ReviewRepositoryAdapter implements ReviewRepository {

    private final ReviewJpaRepository jpaRepository;
    private final ReviewMapper mapper;

    public ReviewRepositoryAdapter(ReviewJpaRepository jpaRepository, ReviewMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Review save(Review review) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(review)));
    }

    @Override
    public Optional<Review> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Review> findByBookingId(UUID bookingId) {
        return jpaRepository.findByBookingId(bookingId)
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Review> findByRoomId(UUID roomId) {
        return jpaRepository.findByRoomId(roomId)
                .stream().map(mapper::toDomain).toList();
    }
    @Override
    public boolean existsByBookingIdAndReviewerId(UUID bookingId, UUID reviewerId) {
        return jpaRepository.existsByBookingIdAndReviewerId(bookingId, reviewerId);
    }
    @Override
    public List<Review> findFlaggedPendingReview() {
        return jpaRepository.findFlaggedPendingReview().stream()
        .map(mapper::toDomain)
        .toList();
    }

}
