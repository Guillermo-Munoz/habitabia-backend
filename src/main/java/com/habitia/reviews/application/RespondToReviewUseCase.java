package com.habitia.reviews.application;

import com.habitia.bookings.domain.BookingRepository;
import com.habitia.notifications.application.CreateNotificationUseCase;
import com.habitia.notifications.domain.NotificationType;
import com.habitia.reviews.domain.Review;
import com.habitia.reviews.domain.ReviewRepository;
import com.habitia.shared.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RespondToReviewUseCase {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final CreateNotificationUseCase createNotification;

    public RespondToReviewUseCase(ReviewRepository reviewRepository,
                                  BookingRepository bookingRepository,
                                  CreateNotificationUseCase createNotification) {
        this.reviewRepository = reviewRepository;
        this.bookingRepository = bookingRepository;
        this.createNotification = createNotification;
    }

    /**
     * Permite al anfitrión responder a una reseña recibida.
     * Valida que el hostId del token corresponde al anfitrión de la reserva asociada.
     */
    public Review execute(RespondToReviewCommand command) {
        Review review = reviewRepository.findById(command.reviewId())
                .orElseThrow(() -> new ResourceNotFoundException("Review", command.reviewId().toString()));

        var booking = bookingRepository.findById(review.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking", review.getBookingId().toString()));

        if (!booking.getHostId().value().equals(command.hostId())) {
            throw new IllegalArgumentException("Host is not the owner of the booking related to this review.");
        }

        review.respondToReview(command.response(), command.hostId());
        Review saved = reviewRepository.save(review);

        createNotification.execute(saved.getReviewerId(), NotificationType.REVIEW_RESPONSE,
                "The host has responded to your review", saved.getId());

        return saved;
    }
}
