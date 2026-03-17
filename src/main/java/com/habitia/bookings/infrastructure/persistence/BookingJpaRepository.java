package com.habitia.bookings.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BookingJpaRepository extends JpaRepository<BookingJpaEntity, UUID> {

    List<BookingJpaEntity> findByGuestId(UUID guestId);

    List<BookingJpaEntity> findByHostId(UUID hostId);

    @Query("SELECT COUNT(b) > 0 FROM BookingJpaEntity b " +
            "WHERE b.roomId = :roomId " +
            "AND b.status IN ('REQUESTED', 'ACCEPTED', 'CONFIRMED') " +
            "AND b.checkIn < :checkOut AND b.checkOut > :checkIn")
    boolean existsOverlap(@Param("roomId") UUID roomId,
                          @Param("checkIn") LocalDate checkIn,
                          @Param("checkOut") LocalDate checkOut);
}