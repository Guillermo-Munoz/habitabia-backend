package com.habitia.reviews.application;

import com.habitia.reviews.domain.Review;
import com.habitia.reviews.domain.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GetReviewsByBookingUseCase {

    private final ReviewRepository reviewRepository;

    public GetReviewsByBookingUseCase(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> execute(UUID bookingId) {
        return reviewRepository.findByBookingId(bookingId);
    }
}
