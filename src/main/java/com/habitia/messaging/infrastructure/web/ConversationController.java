package com.habitia.messaging.infrastructure.web;

import com.habitia.messaging.application.GetMessagesUseCase;
import com.habitia.messaging.application.SendMessageUseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/conversations")
public class ConversationController {

    private final SendMessageUseCase sendMessage;
    private final GetMessagesUseCase getMessages;

    public ConversationController(SendMessageUseCase sendMessage, GetMessagesUseCase getMessages) {
        this.sendMessage = sendMessage;
        this.getMessages = getMessages;
    }

    @PostMapping("/{bookingId}/messages")
    public ResponseEntity<MessageResponse> send(
            @PathVariable UUID bookingId,
            @RequestBody SendMessageRequest request,
            Authentication auth) {
        var message = sendMessage.execute(bookingId, UUID.fromString(auth.getName()), request.content());
        return ResponseEntity.status(HttpStatus.CREATED).body(MessageResponse.from(message));
    }

    @GetMapping("/{bookingId}/messages")
    public ResponseEntity<Page<MessageResponse>> getMessages(
            @PathVariable UUID bookingId,
            @PageableDefault(size = 20, sort = "sentAt", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(getMessages.execute(bookingId, pageable).map(MessageResponse::from));
    }
}
