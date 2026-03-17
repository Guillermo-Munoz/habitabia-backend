package com.habitia.bookings.infrastructure.web;

import com.habitia.bookings.application.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final RequestBookingUseCase requestBookingUseCase;
    private final UpdateBookingStatusUseCase updateBookingStatusUseCase;
    private final GetBookingsAsGuestUseCase getBookingsAsGuestUseCase;
    private final GetBookingsAsHostUseCase getBookingsAsHostUseCase;

    public BookingController(
            RequestBookingUseCase requestBookingUseCase,
            UpdateBookingStatusUseCase updateBookingStatusUseCase,
            GetBookingsAsGuestUseCase getBookingsAsGuestUseCase,
            GetBookingsAsHostUseCase getBookingsAsHostUseCase) {
        this.requestBookingUseCase = requestBookingUseCase;
        this.updateBookingStatusUseCase = updateBookingStatusUseCase;
        this.getBookingsAsGuestUseCase = getBookingsAsGuestUseCase;
        this.getBookingsAsHostUseCase = getBookingsAsHostUseCase;
    }

    @PostMapping
    public ResponseEntity<BookingResponse> request(
            @Valid @RequestBody BookingRequest request,
            Authentication auth) {
        var booking = requestBookingUseCase.execute(new RequestBookingCommand(
                auth.getName(),
                request.roomId(),
                request.hostId(),
                request.checkIn(),
                request.checkOut(),
                request.guests(),
                request.message()
        ));
        return ResponseEntity.status(HttpStatus.CREATED).body(BookingResponse.from(booking));
    }

    @PatchMapping("/{id}/accept")
    public ResponseEntity<BookingResponse> accept(@PathVariable UUID id) {
        return ResponseEntity.ok(BookingResponse.from(updateBookingStatusUseCase.accept(id)));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<BookingResponse> reject(@PathVariable UUID id) {
        return ResponseEntity.ok(BookingResponse.from(updateBookingStatusUseCase.reject(id)));
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<BookingResponse> confirm(@PathVariable UUID id) {
        return ResponseEntity.ok(BookingResponse.from(updateBookingStatusUseCase.confirm(id)));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<BookingResponse> cancel(@PathVariable UUID id) {
        return ResponseEntity.ok(BookingResponse.from(updateBookingStatusUseCase.cancel(id)));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<BookingResponse> complete(@PathVariable UUID id) {
        return ResponseEntity.ok(BookingResponse.from(updateBookingStatusUseCase.complete(id)));
    }

    @GetMapping("/guest/me")
    public ResponseEntity<List<BookingResponse>> getMyBookingsAsGuest(Authentication auth) {
        var bookings = getBookingsAsGuestUseCase.execute(auth.getName());
        return ResponseEntity.ok(bookings.stream().map(BookingResponse::from).toList());
    }

    @GetMapping("/host/me")
    public ResponseEntity<List<BookingResponse>> getMyBookingsAsHost(Authentication auth) {
        var bookings = getBookingsAsHostUseCase.execute(auth.getName());
        return ResponseEntity.ok(bookings.stream().map(BookingResponse::from).toList());
    }
}