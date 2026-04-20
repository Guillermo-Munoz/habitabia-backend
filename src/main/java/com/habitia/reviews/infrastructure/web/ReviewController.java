package com.habitia.reviews.infrastructure.web;

import com.habitia.reviews.application.GetReviewsByBookingUseCase;
import com.habitia.reviews.application.GetReviewsByRoomUseCase;
import com.habitia.reviews.application.RespondToReviewCommand;
import com.habitia.reviews.application.RespondToReviewUseCase;
import com.habitia.reviews.application.SubmitReviewCommand;
import com.habitia.reviews.application.SubmitReviewUseCase;
import com.habitia.reviews.domain.RoomRatingStats;
import com.habitia.reviews.application.GetRoomRatingUseCase;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    private final RespondToReviewUseCase respondToReviewUseCase;
    private final GetRoomRatingUseCase getRoomRatingUseCase;

    public ReviewController(SubmitReviewUseCase submitReviewUseCase,
                            GetReviewsByBookingUseCase getReviewsByBookingUseCase,
                            GetReviewsByRoomUseCase getReviewsByRoomUseCase,
                            RespondToReviewUseCase respondToReviewUseCase,
                            GetRoomRatingUseCase getRoomRatingUseCase) {
        this.submitReviewUseCase = submitReviewUseCase;
        this.getReviewsByBookingUseCase = getReviewsByBookingUseCase;
        this.getReviewsByRoomUseCase = getReviewsByRoomUseCase;
        this.respondToReviewUseCase = respondToReviewUseCase;
        this.getRoomRatingUseCase = getRoomRatingUseCase;
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
                request.isReviewForHost(),
                request.isPublic()
        ));
        return ResponseEntity.status(HttpStatus.CREATED).body(ReviewResponse.from(review));
    }

    @PatchMapping("/{id}/respond")
    public ResponseEntity<ReviewResponse> respond(
            @PathVariable UUID id,
            @RequestBody RespondToReviewRequest request,
            Authentication auth) {
        var review = respondToReviewUseCase.execute(new RespondToReviewCommand(
                id,
                UUID.fromString(auth.getName()),
                request.response()
        ));
        return ResponseEntity.ok(ReviewResponse.from(review));
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<ReviewResponse>> getByBooking(@PathVariable UUID bookingId) {
        var reviews = getReviewsByBookingUseCase.execute(bookingId);
        return ResponseEntity.ok(reviews.stream().map(ReviewResponse::from).toList());
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<Page<ReviewResponse>> getByRoom(
            @PathVariable UUID roomId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(getReviewsByRoomUseCase.execute(roomId, pageable).map(ReviewResponse::from));
    }
    @GetMapping("/room/{roomId}/rating")
    public ResponseEntity<RoomRatingStats> getRating(@PathVariable UUID roomId) {
    return ResponseEntity.ok(getRoomRatingUseCase.execute(roomId));
}
}
