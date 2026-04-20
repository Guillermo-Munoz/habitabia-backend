package com.habitia.reviews.application;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.habitia.reviews.domain.ReviewRepository;
import com.habitia.reviews.domain.RoomRatingStats;

@Service
public class GetRoomRatingUseCase {
    private final ReviewRepository reviewRepository;
    
    public GetRoomRatingUseCase(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }
    public RoomRatingStats execute(UUID roomId) {
        return reviewRepository.getRatingStatsByRoomId(roomId);
    }
    
}
