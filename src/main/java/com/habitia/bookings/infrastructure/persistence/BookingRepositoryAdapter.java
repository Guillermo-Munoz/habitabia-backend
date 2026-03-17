package com.habitia.bookings.infrastructure.persistence;

import com.habitia.bookings.domain.Booking;

import com.habitia.bookings.domain.BookingRepository;
import com.habitia.bookings.domain.BookingStatus;
import com.habitia.shared.domain.valueobject.UserId;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class BookingRepositoryAdapter implements BookingRepository {

    private final BookingJpaRepository jpaRepository;

    public BookingRepositoryAdapter(BookingJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Booking save(Booking booking) {
        return toDomain(jpaRepository.save(toEntity(booking)));
    }

    @Override
    public Optional<Booking> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Booking> findByGuestId(UserId guestId) {
        return jpaRepository.findByGuestId(guestId.value())
                .stream().map(this::toDomain).toList();
    }

    @Override
    public List<Booking> findByHostId(UserId hostId) {
        return jpaRepository.findByHostId(hostId.value())
                .stream().map(this::toDomain).toList();
    }

    @Override
    public boolean existsOverlap(UUID roomId, LocalDate checkIn, LocalDate checkOut) {
        return jpaRepository.existsOverlap(roomId, checkIn, checkOut);
    }

    private BookingJpaEntity toEntity(Booking b) {
        BookingJpaEntity e = new BookingJpaEntity();
        e.setId(b.getId());
        e.setRoomId(b.getRoomId());
        e.setGuestId(b.getGuestId().value());
        e.setHostId(b.getHostId().value());
        e.setCheckIn(b.getDateRange().checkIn());
        e.setCheckOut(b.getDateRange().checkOut());
        e.setGuests(b.getGuests());
        e.setStatus(b.getStatus().name());
        e.setMessage(b.getMessage());
        e.setCreatedAt(b.getCreatedAt());
        return e;
    }

    private Booking toDomain(BookingJpaEntity e) {
        return new Booking(
                e.getId(),
                e.getRoomId(),
                new UserId(e.getGuestId()),
                new UserId(e.getHostId()),
                e.getCheckIn(),
                e.getCheckOut(),
                e.getGuests(),
                BookingStatus.valueOf(e.getStatus()),
                e.getMessage(),
                e.getCreatedAt()
        );
    }
}