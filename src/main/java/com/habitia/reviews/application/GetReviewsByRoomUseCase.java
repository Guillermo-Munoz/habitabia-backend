package com.habitia.reviews.application;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.habitia.reviews.domain.Review;
import com.habitia.reviews.domain.ReviewRepository;

@Service
public class GetReviewsByRoomUseCase {
    private final ReviewRepository reviewRepository;

    public GetReviewsByRoomUseCase(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> execute(UUID roomId){
        return reviewRepository.findByRoomId(roomId);
    }
}
