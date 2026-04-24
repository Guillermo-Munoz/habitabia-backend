package com.habitia.rooms.application;

import com.habitia.rooms.domain.Room;
import com.habitia.rooms.domain.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchRoomsUseCase {

    private final RoomRepository roomRepository;

    public SearchRoomsUseCase(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> execute(String city, int guests) {
        if (city == null || city.isBlank()) {
            return roomRepository.searchAvailableAll(guests);
        }
        return roomRepository.searchAvailable(city, guests);
    }
}