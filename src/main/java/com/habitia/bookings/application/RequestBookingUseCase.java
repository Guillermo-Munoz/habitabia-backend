package com.habitia.bookings.application;

import com.habitia.bookings.domain.Booking;
import com.habitia.bookings.domain.BookingRepository;
import com.habitia.shared.domain.exception.BusinessRuleException;
import com.habitia.shared.domain.valueobject.UserId;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RequestBookingUseCase {

    private final BookingRepository bookingRepository;

    public RequestBookingUseCase(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking execute(RequestBookingCommand command) {

        //Validación: guest y host no pueden ser el mismo
        if (command.guestId().equals(command.hostId())) {
            throw new BusinessRuleException("Guest and host cannot be the same user");
        }

        if (bookingRepository.existsOverlap(
                UUID.fromString(command.roomId()),
                command.checkIn(),
                command.checkOut())) {
            throw new BusinessRuleException("Room is not available for the selected dates");
        }

        Booking booking = new Booking(
                UUID.fromString(command.roomId()),
                UserId.of(command.guestId()),
                UserId.of(command.hostId()),
                command.checkIn(),
                command.checkOut(),
                command.guests(),
                command.message()
        );

        return bookingRepository.save(booking);
    }
}