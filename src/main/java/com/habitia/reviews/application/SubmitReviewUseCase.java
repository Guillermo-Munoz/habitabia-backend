package com.habitia.reviews.application;

import com.habitia.bookings.domain.Booking;
import com.habitia.bookings.domain.BookingRepository;
import com.habitia.notifications.application.CreateNotificationUseCase;
import com.habitia.notifications.domain.NotificationType;
import com.habitia.reviews.domain.Review;
import com.habitia.reviews.domain.ReviewRepository;
import com.habitia.shared.domain.exception.BusinessRuleException;
import com.habitia.shared.domain.exception.ResourceNotFoundException;
import com.habitia.shared.domain.moderation.ContentModerationService;
import com.habitia.shared.domain.moderation.ModerationResult;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SubmitReviewUseCase {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final ContentModerationService moderationService;
    private final CreateNotificationUseCase createNotification;

    public SubmitReviewUseCase(ReviewRepository reviewRepository,
                               BookingRepository bookingRepository,
                               ContentModerationService moderationService,
                               CreateNotificationUseCase createNotification) {
        this.reviewRepository = reviewRepository;
        this.bookingRepository = bookingRepository;
        this.moderationService = moderationService;
        this.createNotification = createNotification;
    }

    public Review execute(SubmitReviewCommand command) {
        Booking booking = bookingRepository.findById(command.bookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking", command.bookingId().toString()));

        if (reviewRepository.existsByBookingIdAndReviewerId(
                command.bookingId(), command.reviewerId())) {
            throw new BusinessRuleException("Reviewer has already submitted a review for this booking");
        }

        Review review = new Review(
                command.bookingId(),
                command.reviewerId(),
                command.rating(),
                command.comment(),
                command.isReviewForHost(),
                command.isPublic()
        );

        ModerationResult moderation = moderationService.analyze(command.comment());
        if (moderation.passed()) {
            review.approve();
        } else {
            review.flag(moderation.reason());
        }

        Review saved = reviewRepository.save(review);

        // Notificar al destinatario de la reseña
        UUID recipientId = command.isReviewForHost()
                ? booking.getHostId().value()
                : booking.getGuestId().value();
        createNotification.execute(recipientId, NotificationType.REVIEW_RECEIVED,
                "You have received a new review", saved.getId());

        return saved;
    }
}
