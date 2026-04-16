package com.habitia.reviews.application;

import com.habitia.notifications.application.CreateNotificationUseCase;
import com.habitia.notifications.domain.NotificationType;
import com.habitia.reviews.domain.Review;
import com.habitia.reviews.domain.ReviewRepository;
import com.habitia.shared.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ApproveReviewUseCase {

    private final ReviewRepository reviewRepository;
    private final CreateNotificationUseCase createNotification;

    public ApproveReviewUseCase(ReviewRepository reviewRepository,
                                CreateNotificationUseCase createNotification) {
        this.reviewRepository = reviewRepository;
        this.createNotification = createNotification;
    }

    public Review execute(UUID reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", reviewId.toString()));

        review.unflag();
        review.approve();

        Review saved = reviewRepository.save(review);

        createNotification.execute(saved.getReviewerId(), NotificationType.REVIEW_APPROVED,
                "Your review has been approved", saved.getId());

        return saved;
    }
}