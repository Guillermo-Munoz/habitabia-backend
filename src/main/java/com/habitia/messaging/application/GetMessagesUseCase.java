package com.habitia.messaging.application;

import com.habitia.messaging.domain.Message;
import com.habitia.messaging.domain.ConversationRepository;
import com.habitia.messaging.domain.MessageRepository;
import com.habitia.shared.domain.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetMessagesUseCase {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;

    public GetMessagesUseCase(MessageRepository messageRepository,
                              ConversationRepository conversationRepository) {
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
    }

    public Page<Message> execute(UUID bookingId, Pageable pageable) {
        var conversation = conversationRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation", bookingId.toString()));

        return messageRepository.findByConversationId(conversation.getId(), pageable);
    }
}
