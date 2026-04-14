package com.habitia.messaging.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Message {
    private final UUID id;
    private final UUID conversationId;
    private final UUID senderId;
    private final String content;
    private final LocalDateTime sentAt;
    private boolean isRead;

    public Message(UUID conversationId, UUID senderId, String content){
        if(conversationId == null){ throw new IllegalArgumentException("Conversation ID cannot be null");}
        if(senderId == null){ throw new IllegalArgumentException("Sender ID cannot be null");}
        if(content == null || content.isBlank()){ throw new IllegalArgumentException("Content cannot be empty");}
        if(content.length() > 2000){ throw new IllegalArgumentException("Content cannot exceed 2000 characters");}
        this.id = UUID.randomUUID();
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.content = content;
        this.sentAt = LocalDateTime.now();
        this.isRead = false;
    }

    /** Reconstruye desde la persistencia */
    public Message(UUID id, UUID conversationId, UUID senderId, String content, LocalDateTime sentAt, boolean isRead){
        this.id = id;
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.content = content;
        this.sentAt = sentAt;
        this.isRead = isRead;
    }
    public void markAsRead(){
        this.isRead = true;
    }
        public UUID getId() { return id; }
    public UUID getConversationId() { return conversationId; }
    public UUID getSenderId() { return senderId; }
    public String getContent() { return content; }
    public LocalDateTime getSentAt() { return sentAt; }
    public boolean isRead() { return isRead; }
    
}
