package com.habitia.reviews.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
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
    @DisplayName("should update comment successfully")
    void shouldUpdateCommentSuccessfully() {
        review.updateComment("Updated comment", reviewerId);

        assertAll(
            () -> assertEquals("Updated comment", review.getComment()),
            () -> assertTrue(review.isEdited()),
            () -> assertEquals(1, review.getEditCount()),
            () -> assertNotNull(review.getEditedAt())
        );
    }

    @Test
    @DisplayName("should trim comment when updating")
    void shouldTrimComment_whenUpdating() {
        review.updateComment("   Updated comment   ", reviewerId);

        assertEquals("Updated comment", review.getComment());
    }

    @Test
    @DisplayName("should increment edit count when updating comment multiple times")
    void shouldIncrementEditCount_whenUpdatingMultipleTimes() {
        review.updateComment("First update", reviewerId);
        review.updateComment("Second update", reviewerId);

        assertEquals(2, review.getEditCount());
    }

    @Test
    @DisplayName("should throw exception when requester is not reviewer")
    void shouldThrowException_whenRequesterIsNotReviewer() {
        UUID otherUserId = UUID.randomUUID();

        assertThrows(IllegalStateException.class,
            () -> review.updateComment("New comment", otherUserId));
    }

    @Test
    @DisplayName("should throw exception when updating deleted review")
    void shouldThrowException_whenReviewIsDeleted() {
        review.softDelete(reviewerId);

        assertThrows(IllegalStateException.class,
            () -> review.updateComment("New comment", reviewerId));
    }

    @Test
    @DisplayName("should throw exception when approved review is older than seven days")
    void shouldThrowException_whenApprovedReviewIsOlderThanSevenDays() {

        LocalDateTime eightDaysAgo = LocalDateTime.now().minusDays(8);

        Review oldApprovedReview = new Review(
            UUID.randomUUID(),
            bookingId,
            reviewerId,
            4,
            "comment",
            eightDaysAgo,
            true,
            true,
            true,   // approved
            false,
            false,
            false,
            false,
            false,
            LocalDateTime.now(),
            null,
            null,
            null,
            0,
            null,
            null,
            null
        );

        assertThrows(IllegalStateException.class,
            () -> oldApprovedReview.updateComment("New comment", reviewerId));
    }

    @Test
    @DisplayName("should allow updating approved review within seven days")
    void shouldAllowUpdatingApprovedReviewWithinSevenDays() {

        LocalDateTime twoDaysAgo = LocalDateTime.now().minusDays(2);

        Review recentApprovedReview = new Review(
            UUID.randomUUID(),
            bookingId,
            reviewerId,
            4,
            "comment",
            twoDaysAgo,
            true,
            true,
            true,   // approved
            false,
            false,
            false,
            false,
            false,
            LocalDateTime.now(),
            null,
            null,
            null,
            0,
            null,
            null,
            null
        );

        recentApprovedReview.updateComment("Updated comment", reviewerId);

        assertAll(
            () -> assertEquals("Updated comment", recentApprovedReview.getComment()),
            () -> assertTrue(recentApprovedReview.isEdited()),
            () -> assertEquals(1, recentApprovedReview.getEditCount()),
            () -> assertNotNull(recentApprovedReview.getEditedAt())
        );
    }

    @Test
    @DisplayName("should throw exception when comment is null")
    void shouldThrowException_whenCommentIsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> review.updateComment(null, reviewerId));
    }

    @Test
    @DisplayName("should throw exception when comment is empty")
    void shouldThrowException_whenCommentIsEmpty() {
        assertThrows(IllegalArgumentException.class,
            () -> review.updateComment("", reviewerId));
    }

    @Test
    @DisplayName("should throw exception when comment exceeds max length")
    void shouldThrowException_whenCommentExceedsMaxLength() {
        String longComment = "a".repeat(1001);

        assertThrows(IllegalArgumentException.class,
            () -> review.updateComment(longComment, reviewerId));
    }

    // -------------------------------------------------------------------------
    // responseToREview
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("should throw exception when hostId is null")
    void shouldThrowException_whenHostIsNull(){
        assertThrows(IllegalArgumentException.class,
            () -> review.respondToReview("Response", null));
    }
    @Test
    @DisplayName("should throw exception when review is not for host")
    void sholdThrowException_whenRevieweIsNotForHost(){
        Review reviewNotForHost = new Review(
            bookingId,
            reviewerId,
            4,
            "comment",
            false,
            true
        );
        assertThrows(IllegalStateException.class,
            () -> reviewNotForHost.respondToReview("Thanks", UUID.randomUUID()));
    }
    @Test
    @DisplayName("should throw exception when review si delete")
    void shouldThrowExceptionWhenReviewIsDelete(){
        review.softDelete(reviewerId);
        assertThrows(IllegalStateException.class,
            () -> review.respondToReview("comment", UUID.randomUUID()));
        
    }
    @Test
    @DisplayName("should throw exception when review is already responded")
    void shouldThrowExceptionWhenReviewIsResponded(){
        UUID hostId = UUID.randomUUID();
        review.respondToReview("first response", hostId);
        assertThrows(IllegalStateException.class,
            () -> review.respondToReview("second response", hostId));
    }














}

