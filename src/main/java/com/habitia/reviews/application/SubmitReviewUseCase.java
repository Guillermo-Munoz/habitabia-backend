package com.habitia.reviews.application;

import com.habitia.bookings.domain.BookingRepository;
import com.habitia.reviews.domain.Review;
import com.habitia.reviews.domain.ReviewRepository;
import com.habitia.shared.domain.exception.BusinessRuleException;
import com.habitia.shared.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SubmitReviewUseCase {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;

    public SubmitReviewUseCase(ReviewRepository reviewRepository,
                               BookingRepository bookingRepository) {
        this.reviewRepository = reviewRepository;
        this.bookingRepository = bookingRepository;
    }

    public Review execute(SubmitReviewCommand command) {
        if (!bookingRepository.findById(command.bookingId()).isPresent()) {
            throw new ResourceNotFoundException("Booking", command.bookingId().toString());
        }

        if (reviewRepository.existsByBookingIdAndReviewerId(
                command.bookingId(), command.reviewerId())) {
            throw new BusinessRuleException("Reviewer has already submitted a review for this booking");
        }

        Review review = new Review(
                command.bookingId(),
                command.reviewerId(),
                command.rating(),
                command.comment(),
                command.isHostReview(),
                command.isPublic()
        );

        return reviewRepository.save(review);
    }
}
