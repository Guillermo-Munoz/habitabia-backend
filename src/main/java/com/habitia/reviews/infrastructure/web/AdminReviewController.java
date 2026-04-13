package com.habitia.reviews.infrastructure.web;

import com.habitia.reviews.application.ApproveReviewUseCase;
import com.habitia.reviews.application.GetFlaggedReviewsUseCase;
import com.habitia.reviews.application.RejectReviewUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/reviews")
@PreAuthorize("hasRole('ADMIN')")
public class AdminReviewController {

    private final GetFlaggedReviewsUseCase getFlaggedReviews;
    private final ApproveReviewUseCase approveReview;
    private final RejectReviewUseCase rejectReview;

    public AdminReviewController(GetFlaggedReviewsUseCase getFlaggedReviews,
                                 ApproveReviewUseCase approveReview,
                                 RejectReviewUseCase rejectReview) {
        this.getFlaggedReviews = getFlaggedReviews;
        this.approveReview = approveReview;
        this.rejectReview = rejectReview;
    }

    @GetMapping("/flagged")
    public ResponseEntity<List<ReviewResponse>> getFlagged() {
        List<ReviewResponse> response = getFlaggedReviews.execute().stream()
                .map(ReviewResponse::from)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<ReviewResponse> approve(@PathVariable UUID id) {
        return ResponseEntity.ok(ReviewResponse.from(approveReview.execute(id)));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<Void> reject(@PathVariable UUID id) {
        rejectReview.execute(id);
        return ResponseEntity.noContent().build();
    }
}
