package com.habitia.bookings.application;

import com.habitia.bookings.domain.Booking;
import com.habitia.bookings.domain.BookingRepository;
import com.habitia.shared.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateBookingStatusUseCase {

    private final BookingRepository bookingRepository;

    public UpdateBookingStatusUseCase(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking accept(UUID bookingId) {
        Booking booking = findOrThrow(bookingId);
        booking.accept();
        return bookingRepository.save(booking);
    }

    public Booking reject(UUID bookingId) {
        Booking booking = findOrThrow(bookingId);
        booking.reject();
        return bookingRepository.save(booking);
    }

    public Booking confirm(UUID bookingId) {
        Booking booking = findOrThrow(bookingId);
        booking.confirm();
        return bookingRepository.save(booking);
    }

    public Booking cancel(UUID bookingId) {
        Booking booking = findOrThrow(bookingId);
        booking.cancel();
        return bookingRepository.save(booking);
    }

    public Booking complete(UUID bookingId) {
        Booking booking = findOrThrow(bookingId);
        booking.complete();
        return bookingRepository.save(booking);
    }

    private Booking findOrThrow(UUID id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", id.toString()));
    }
}