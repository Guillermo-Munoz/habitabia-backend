package com.habitia.rooms.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface RoomJpaRepository extends JpaRepository<RoomJpaEntity, UUID> {

    List<RoomJpaEntity> findByHostId(UUID hostId);

    @Query("SELECT r FROM RoomJpaEntity r WHERE r.city = :city " +
            "AND r.maxGuests >= :guests AND r.status = 'ACTIVE'")
    List<RoomJpaEntity> searchAvailable(@Param("city") String city,
                                        @Param("guests") int guests);

    @Query("SELECT DISTINCT r.city FROM RoomJpaEntity r WHERE r.status = 'ACTIVE' ORDER BY r.city")
    List<String> findAvailableCities();

    @Query("SELECT r FROM RoomJpaEntity r WHERE r.status = 'ACTIVE' " +
           "AND r.maxGuests >= :guests " +
           "AND NOT EXISTS (" +
           "  SELECT b FROM BookingJpaEntity b " +
           "  WHERE b.roomId = r.id " +
           "  AND b.status IN ('REQUESTED', 'ACCEPTED', 'CONFIRMED') " +
           "  AND b.checkIn < :checkOut AND b.checkOut > :checkIn" +
           ")")
    List<RoomJpaEntity> findAvailableByDates(@Param("checkIn") LocalDate checkIn,
                                             @Param("checkOut") LocalDate checkOut,
                                             @Param("guests") int guests);
}