package com.habitia.reviews.infrastructure.web;

import com.habitia.reviews.application.GetReviewsByBookingUseCase;
import com.habitia.reviews.application.GetReviewsByRoomUseCase;
import com.habitia.reviews.application.SubmitReviewCommand;
import com.habitia.reviews.application.SubmitReviewUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final SubmitReviewUseCase submitReviewUseCase;
    private final GetReviewsByBookingUseCase getReviewsByBookingUseCase;
    private final GetReviewsByRoomUseCase getReviewsByRoomUseCase;

    public ReviewController(SubmitReviewUseCase submitReviewUseCase,
                            GetReviewsByBookingUseCase getReviewsByBookingUseCase,
                            GetReviewsByRoomUseCase getReviewsByRoomUseCase) {
        this.submitReviewUseCase = submitReviewUseCase;
        this.getReviewsByBookingUseCase = getReviewsByBookingUseCase;
        this.getReviewsByRoomUseCase = getReviewsByRoomUseCase;
    }

    @PostMapping
    public ResponseEntity<ReviewResponse> submit(
            @RequestBody SubmitReviewRequest request,
            Authentication auth) {
        var review = submitReviewUseCase.execute(new SubmitReviewCommand(
                request.bookingId(),
                UUID.fromString(auth.getName()),
                request.rating(),
                request.comment(),
                request.isHostReview(),
                request.isPublic()
        ));
        return ResponseEntity.status(HttpStatus.CREATED).body(ReviewResponse.from(review));
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<ReviewResponse>> getByBooking(@PathVariable UUID bookingId) {
        var reviews = getReviewsByBookingUseCase.execute(bookingId);
        return ResponseEntity.ok(reviews.stream().map(ReviewResponse::from).toList());
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<ReviewResponse>> getByRoom(@PathVariable UUID roomId) {
        var reviews = getReviewsByRoomUseCase.execute(roomId);
        return ResponseEntity.ok(reviews.stream().map(ReviewResponse::from).toList());
    }
}
