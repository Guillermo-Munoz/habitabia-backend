package com.habitia.bookings.application;

import com.habitia.bookings.domain.Booking;
import com.habitia.bookings.domain.BookingRepository;
import com.habitia.shared.domain.valueobject.UserId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetBookingsAsGuestUseCase {

    private final BookingRepository bookingRepository;

    public GetBookingsAsGuestUseCase(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public List<Booking> execute(String userId) {
        return bookingRepository.findByGuestId(UserId.of(userId));
    }
}