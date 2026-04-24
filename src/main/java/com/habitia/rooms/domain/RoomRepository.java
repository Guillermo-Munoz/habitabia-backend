package com.habitia.rooms.domain;

import com.habitia.shared.domain.valueobject.UserId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomRepository {
    Room save(Room room);
    Optional<Room> findById(UUID id);
    List<Room> findByHostId(UserId hostId);
    List<Room> searchAvailable(String city, int guests);
    List<Room> searchAvailableAll(int guests);
    List<String> findAvailableCities();
    List<Room> findAvailableByDates(LocalDate checkIn, LocalDate checkOut, int guests);
}