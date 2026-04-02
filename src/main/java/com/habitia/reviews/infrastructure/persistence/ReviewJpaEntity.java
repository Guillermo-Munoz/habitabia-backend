package com.habitia.reviews.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
public class ReviewJpaEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false)
    private UUID bookingId;

    @Column(nullable = false)
    private UUID reviewerId;

    @Column(nullable = false)
    private int rating;

    @Column(length = 1000)
    private String comment;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean isHostReview;

    @Column(nullable = false)
    private boolean isPublic;

    @Column(nullable = false)
    private boolean isApproved;

    @Column(nullable = false)
    private boolean isDeleted;

    @Column(nullable = false)
    private boolean isFlagged;

    @Column(nullable = false)
    private boolean isVerified;

    @Column(nullable = false)
    private boolean isEdited;

    @Column(nullable = false)
    private boolean isResponded;

    private LocalDateTime approvedAt;
    private LocalDateTime flaggedAt;
    private String flagReason;
    private LocalDateTime editedAt;
    private int editCount;

    @Column(length = 1000)
    private String hostResponse;

    private LocalDateTime respondedAt;
    private LocalDateTime deletedAt;
}
