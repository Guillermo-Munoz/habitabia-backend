package com.habitia.reviews.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.habitia.reviews.domain.Review;
import com.habitia.reviews.domain.ReviewRepository;

@Service
public class GetFlaggedReviewsUseCase {

    private final ReviewRepository reviewRepository;

    public GetFlaggedReviewsUseCase(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> execute() {
        return reviewRepository.findFlaggedPendingReview();
    }
}
