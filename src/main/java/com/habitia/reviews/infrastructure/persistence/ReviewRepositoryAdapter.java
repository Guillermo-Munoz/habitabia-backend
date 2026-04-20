package com.habitia.reviews.infrastructure.persistence;

import com.habitia.reviews.domain.Review;
import com.habitia.reviews.domain.ReviewRepository;
import com.habitia.reviews.domain.RoomRatingStats;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    @Override
    public Page<Review> findByRoomId(UUID roomId, Pageable pageable){
        return jpaRepository.findByRoomId(roomId, pageable)
                            .map(mapper::toDomain);
    }
    @Override
    public Page<Review> findFlaggedPendingReview(Pageable pageable){
        return jpaRepository.findFlaggedPendingReview(pageable)
                            .map(mapper::toDomain);
    }

   @Override
    public RoomRatingStats getRatingStatsByRoomId(UUID roomId) {
    Double average = jpaRepository.findAverageRatingByRoomId(roomId);
    long total = jpaRepository.countApprovedByRoomId(roomId);
    return new RoomRatingStats(average != null ? average : 0.0, total);
}
    
}
