package com.habitia.reviews.application;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.habitia.reviews.domain.Review;
import com.habitia.reviews.domain.ReviewRepository;

@Service
public class GetReviewByRoomUseCase {
    private final ReviewRepository reviewRepository;

    public GetReviewByRoomUseCase(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> execute(UUID roomId){
        return reviewRepository.findByRoomId(roomId);
    }
}
