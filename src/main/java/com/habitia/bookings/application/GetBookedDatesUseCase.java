package com.habitia.bookings.application;

import com.habitia.bookings.domain.Booking;
import com.habitia.bookings.domain.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class GetBookedDatesUseCase {

    private final BookingRepository bookingRepository;

    public GetBookedDatesUseCase(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public List<Booking> execute(UUID roomId) {
        return bookingRepository.findActiveByRoomId(roomId, LocalDate.now());
    }
}
