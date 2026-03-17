package com.habitia.rooms.domain;

import com.habitia.shared.domain.valueobject.UserId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomRepository {
    Room save(Room room);
    Optional<Room> findById(UUID id);
    List<Room> findByHostId(UserId hostId);
    List<Room> searchAvailable(String city, int guests);
}