package com.habitia.reviews.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

public class ReviewTest {
    private UUID bookingId;
    private UUID reviewerId;
    private Review review;

    @BeforeEach
    void setUp(){
        bookingId = UUID.randomUUID();
        reviewerId = UUID.randomUUID();
        review = new Review(bookingId, reviewerId, 4, "Great stay!", true, true );
    }

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("should initialize review with default state")
    void shouldInitializeReviewWithDefaultState() {
        assertAll(
            () -> assertFalse(review.isApproved()),
            () -> assertFalse(review.isDeleted()),
            () -> assertFalse(review.isFlagged()),
            () -> assertFalse(review.isVerified()),
            () -> assertFalse(review.isEdited()),
            () -> assertFalse(review.isResponded()),
            () -> assertEquals(0, review.getEditCount())
        );
    }

    // -------------------------------------------------------------------------
    // Rating validation
    // -------------------------------------------------------------------------

    @ParameterizedTest
    @ValueSource(ints = {0, 6})
    @DisplayName("should throw exception when rating is out of range")
    void shouldThrowException_whenRatingIsOutOfRange(int rating) {
        assertThrows(IllegalArgumentException.class,
            () -> new Review(bookingId, reviewerId, rating, "comment", true, true));
    }
    
    // -------------------------------------------------------------------------
    // approve
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("should approve review successfully")
    void shouldApproveReviewSuccessfully() {
        review.approve();

        assertTrue(review.isApproved());
        assertNotNull(review.getApprovedAt());
    }

    @Test
    @DisplayName("should throw exception when approving already approved review")
    void shouldThrowException_whenReviewIsAlreadyApproved() {
        review.approve();

        assertThrows(IllegalStateException.class,
            () -> review.approve());
    }
       
    // -------------------------------------------------------------------------
    // flag
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("should flag review with reason")
    void shouldFlagReviewWithReason() {
        review.flag("Inappropriate content");

        assertTrue(review.isFlagged());
        assertEquals("Inappropriate content", review.getFlagReason());
        assertNotNull(review.getFlaggedAt());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("should throw exception when flag reason is null or blank")
    void shouldThrowException_whenFlagReasonIsNullOrBlank(String reason) {
        assertThrows(IllegalArgumentException.class,
            () -> review.flag(reason));
    }

    // -------------------------------------------------------------------------
    // unflag
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("should unflag review and clear flag data")
    void shouldUnflagReviewAndClearFlagData() {
        review.flag("Spam");

        review.unflag();

        assertFalse(review.isFlagged());
        assertNull(review.getFlagReason());
        assertNull(review.getFlaggedAt());
    }

    @Test
    @DisplayName("should throw exception when unflagging non-flagged review")
    void shouldThrowException_whenUnflaggingNonFlaggedReview() {
        assertThrows(IllegalStateException.class,
            () -> review.unflag());
    }

    // -------------------------------------------------------------------------
    // updateComment
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("should throw exception when requester is not reviewer")
    void shouldThrowException_whenRequesterIsNoReviewer(){
        UUID otherUserId = UUID.randomUUID();
        assertThrows(IllegalThreadStateException.class,
            () -> review.updateComment("New comment", otherUserId));
    }
   
    @Test
    @DisplayName("should throw exception when update review delete")
    void shouldThrowException_whenUpdateReviewDelete(){
        
    }
 










}

