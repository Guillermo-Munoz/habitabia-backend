package com.habitia.reviews.infrastructure.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewJpaRepository extends JpaRepository<ReviewJpaEntity, UUID> {

    List<ReviewJpaEntity> findByBookingId(UUID bookingId);

    @Query("SELECT r FROM ReviewJpaEntity r " +
           "JOIN BookingJpaEntity b ON r.bookingId = b.id " +
           "WHERE b.roomId = :roomId")
    List<ReviewJpaEntity> findByRoomId(@Param("roomId") UUID roomId);

    @Query("SELECT r FROM ReviewJpaEntity r " +
           "JOIN BookingJpaEntity b ON r.bookingId = b.id " +
           "WHERE b.roomId = :roomId")
    Page<ReviewJpaEntity> findByRoomId(@Param("roomId") UUID roomId, Pageable pageable);

    boolean existsByBookingIdAndReviewerId(UUID bookingId, UUID reviewerId);

    @Query("SELECT r FROM ReviewJpaEntity r WHERE r.isFlagged = true AND r.isApproved = false AND r.isDeleted = false")
    List<ReviewJpaEntity> findFlaggedPendingReview();

    @Query("SELECT r FROM ReviewJpaEntity r WHERE r.isFlagged = true AND r.isApproved = false AND r.isDeleted = false")
    Page<ReviewJpaEntity> findFlaggedPendingReview(Pageable pageable);
    
    @Query("SELECT AVG(r.rating) FROM ReviewJpaEntity r " +
       "JOIN BookingJpaEntity b ON r.bookingId = b.id " +
       "WHERE b.roomId = :roomId AND r.isApproved = true AND r.isDeleted = false")
    Double findAverageRatingByRoomId(@Param("roomId") UUID roomId);

    @Query("SELECT COUNT(r) FROM ReviewJpaEntity r " +
       "JOIN BookingJpaEntity b ON r.bookingId = b.id " +
       "WHERE b.roomId = :roomId AND r.isApproved = true AND r.isDeleted = false")
    long countApprovedByRoomId(@Param("roomId") UUID roomId);

} 
