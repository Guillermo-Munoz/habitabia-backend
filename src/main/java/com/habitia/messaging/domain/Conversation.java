package com.habitia.messaging.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Conversation {
    private final UUID id;
    private final UUID bookingId; 
    private final UUID guestId;
    private final UUID hostId;
    private final LocalDateTime createdAt;

    public Conversation(UUID bookingId, UUID guestId, UUID hostId){
        if(bookingId == null) throw new IllegalArgumentException("Booking ID cannot be null");
        if(guestId == null) throw new IllegalArgumentException("Guest ID cannot be null");
        if(hostId == null) throw new IllegalArgumentException("Host Id cannot be null");
        this.id = UUID.randomUUID();
        this.bookingId = bookingId;
        this.guestId = guestId;
        this.hostId = hostId;
        this.createdAt = LocalDateTime.now();

        }
    /** Reconstruye desde persistencia */
    public Conversation(UUID id, UUID bookingId, UUID guestId, UUID hostId, LocalDateTime createdAt )   {
        this.id = id;
        this.bookingId = bookingId;
        this.guestId = guestId;
        this.hostId = hostId;
        this.createdAt = createdAt;
    }
    public UUID getId(){return id;}
    public UUID getBookingId(){return bookingId;}
    public UUID getGuestId(){return guestId;}
    public UUID getHostId(){return hostId;}
    public LocalDateTime getCreatedAt(){return createdAt;}
}
