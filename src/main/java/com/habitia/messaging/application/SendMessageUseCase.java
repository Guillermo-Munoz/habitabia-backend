package com.habitia.messaging.application;

import com.habitia.bookings.domain.Booking;
import com.habitia.bookings.domain.BookingRepository;
import com.habitia.messaging.domain.Conversation;
import com.habitia.messaging.domain.ConversationRepository;
import com.habitia.messaging.domain.Message;
import com.habitia.messaging.domain.MessageRepository;
import com.habitia.shared.domain.exception.BusinessRuleException;
import com.habitia.shared.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SendMessageUseCase {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final BookingRepository bookingRepository;

    public SendMessageUseCase(MessageRepository messageRepository,
                              ConversationRepository conversationRepository,
                              BookingRepository bookingRepository) {
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
        this.bookingRepository = bookingRepository;
    }

    public Message execute(UUID bookingId, UUID senderId, String content) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", bookingId.toString()));

        UUID guestId = booking.getGuestId().value();
        UUID hostId  = booking.getHostId().value();

        if (!senderId.equals(guestId) && !senderId.equals(hostId)) {
            throw new BusinessRuleException("Only the guest or host of this booking can send messages");
        }

        Conversation conversation = conversationRepository.findByBookingId(bookingId)
                .orElseGet(() -> conversationRepository.save(
                        new Conversation(bookingId, guestId, hostId)));

        return messageRepository.save(new Message(conversation.getId(), senderId, content));
    }
}
