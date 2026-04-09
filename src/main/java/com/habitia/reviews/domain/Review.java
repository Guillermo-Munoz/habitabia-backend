package com.habitia.reviews.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;

@Getter
public class Review {

    private final UUID id;
    private final UUID bookingId;
    private final UUID reviewerId;
    private final int rating;
    private String comment;
    private final LocalDateTime createdAt;
    private boolean isHostReview; // true: reseña del huésped al anfitrión; false: del anfitrión al huésped
    private boolean isPublic;
    private boolean isApproved;
    private boolean isDeleted;
    private boolean isFlagged;
    private boolean isVerified;
    private boolean isEdited;
    private boolean isResponded;
    private LocalDateTime approvedAt;
    private LocalDateTime flaggedAt;
    private String flagReason;
    private LocalDateTime editedAt;
    private int editCount;
    private String hostResponse;
    private LocalDateTime respondedAt;
    private LocalDateTime deletedAt;

    /** Crea una nueva reseña con valores por defecto. */
    public Review(UUID bookingId, UUID reviewerId, int rating,
                  String comment, boolean isHostReview, boolean isPublic) {

        if (bookingId == null) throw new IllegalArgumentException("Booking ID cannot be null");
        if (reviewerId == null) throw new IllegalArgumentException("Reviewer ID cannot be null");
        if (rating < 1 || rating > 5) throw new IllegalArgumentException("Rating must be between 1 and 5");

        this.id = UUID.randomUUID();
        this.bookingId = bookingId;
        this.reviewerId = reviewerId;
        this.rating = rating;
        this.comment = validateComment(comment);
        this.createdAt = LocalDateTime.now();
        this.isHostReview = isHostReview;
        this.isPublic = isPublic;
        this.isApproved = false;
        this.isDeleted = false;
        this.isFlagged = false;
        this.isVerified = false;
        this.isEdited = false;
        this.isResponded = false;
        this.editCount = 0;
    }

    /** Reconstruye una reseña desde persistencia. */
    public Review(UUID id, UUID bookingId, UUID reviewerId, int rating,
                  String comment, LocalDateTime createdAt, boolean isHostReview,
                  boolean isPublic, boolean isApproved, boolean isDeleted,
                  boolean isFlagged, boolean isVerified, boolean isEdited,
                  boolean isResponded, LocalDateTime approvedAt, LocalDateTime flaggedAt,
                  String flagReason, LocalDateTime editedAt, int editCount,
                  String hostResponse, LocalDateTime respondedAt, LocalDateTime deletedAt) {

        if (id == null) throw new IllegalArgumentException("Id cannot be null");
        if (bookingId == null) throw new IllegalArgumentException("Booking ID cannot be null");
        if (reviewerId == null) throw new IllegalArgumentException("Reviewer ID cannot be null");
        if (rating < 1 || rating > 5) throw new IllegalArgumentException("Rating must be between 1 and 5");

        this.id = id;
        this.bookingId = bookingId;
        this.reviewerId = reviewerId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
        this.isHostReview = isHostReview;
        this.isPublic = isPublic;
        this.isApproved = isApproved;
        this.isDeleted = isDeleted;
        this.isFlagged = isFlagged;
        this.isVerified = isVerified;
        this.isEdited = isEdited;
        this.isResponded = isResponded;
        this.approvedAt = approvedAt;
        this.flaggedAt = flaggedAt;
        this.flagReason = flagReason;
        this.editedAt = editedAt;
        this.editCount = editCount;
        this.hostResponse = hostResponse;
        this.respondedAt = respondedAt;
        this.deletedAt = deletedAt;
    }

    public void approve() {
        if (this.isDeleted) throw new IllegalStateException("Cannot approve a deleted review");
        if (this.isApproved) throw new IllegalStateException("Review is already approved");
        this.isApproved = true;
        this.approvedAt = LocalDateTime.now();
    }

    public void verify() {
        if (this.isDeleted) throw new IllegalStateException("Cannot verify a deleted review");
        if (this.isVerified) throw new IllegalStateException("Review is already verified");
        this.isVerified = true;
    }

    public void flag(String reason) {
        if (this.isFlagged) throw new IllegalStateException("Review is already flagged");
        if (reason == null || reason.trim().isEmpty()) throw new IllegalArgumentException("Flag reason cannot be empty");
        this.isFlagged = true;
        this.flaggedAt = LocalDateTime.now();
        this.flagReason = reason.trim();
    }

    public void unflag() {
        if (!this.isFlagged) throw new IllegalStateException("Review is not flagged");
        this.isFlagged = false;
        this.flaggedAt = null;
        this.flagReason = null;
    }

    public void updateComment(String newComment, UUID requesterId) {
        if (!this.reviewerId.equals(requesterId)) throw new IllegalStateException("Only the reviewer can edit their comment");
        if (this.isDeleted) throw new IllegalStateException("Cannot edit a deleted review");
        if (this.isApproved && this.createdAt.isBefore(LocalDateTime.now().minusDays(7)))
            throw new IllegalStateException("Cannot edit an approved review after 7 days");
        this.comment = validateComment(newComment);
        this.isEdited = true;
        this.editedAt = LocalDateTime.now();
        this.editCount++;
    }

    /**
     * Permite al anfitrión responder a una reseña recibida.
     * La capa de servicio debe verificar que hostId corresponde al anfitrión de la reserva.
     */
    public void respondToReview(String response, UUID hostId) {
        if (hostId == null) throw new IllegalArgumentException("Host ID cannot be null");
        if (!this.isHostReview) throw new IllegalStateException("Only reviews directed to the host can be responded to");
        if (this.isDeleted) throw new IllegalStateException("Cannot respond to a deleted review");
        if (this.isResponded) throw new IllegalStateException("Review has already been responded to");
        if (response == null || response.trim().isEmpty()) throw new IllegalArgumentException("Response cannot be empty");
        this.hostResponse = response.trim();
        this.isResponded = true;
        this.respondedAt = LocalDateTime.now();
    }

    public void softDelete(UUID requesterId) {
        if (!this.reviewerId.equals(requesterId)) throw new IllegalStateException("Only the reviewer can delete their review");
        if (this.isDeleted) throw new IllegalStateException("Review is already deleted");
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    private String validateComment(String comment) {
        if (comment == null || comment.trim().isEmpty()) throw new IllegalArgumentException("Comment cannot be empty");
        if (comment.length() > 1000) throw new IllegalArgumentException("Comment cannot exceed 1000 characters");
        return comment.trim();
    }
}
