package com.habitia.rooms.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface RoomJpaRepository extends JpaRepository<RoomJpaEntity, UUID> {

    List<RoomJpaEntity> findByHostId(UUID hostId);

    @Query("SELECT r FROM RoomJpaEntity r WHERE r.city = :city " +
            "AND r.maxGuests >= :guests AND r.status = 'ACTIVE'")
    List<RoomJpaEntity> searchAvailable(@Param("city") String city,
                                        @Param("guests") int guests);
}