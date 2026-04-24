package com.habitia.rooms.application;

import com.habitia.rooms.domain.Room;
import com.habitia.rooms.domain.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class GetAvailableRoomsByDatesUseCase {

    private final RoomRepository roomRepository;

    public GetAvailableRoomsByDatesUseCase(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> execute(LocalDate checkIn, LocalDate checkOut, int guests) {
        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("checkOut must be after checkIn");
        }
        return roomRepository.findAvailableByDates(checkIn, checkOut, guests);
    }
}
