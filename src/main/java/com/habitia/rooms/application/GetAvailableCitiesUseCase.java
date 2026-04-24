package com.habitia.rooms.application;

import com.habitia.rooms.domain.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAvailableCitiesUseCase {

    private final RoomRepository roomRepository;

    public GetAvailableCitiesUseCase(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<String> execute() {
        return roomRepository.findAvailableCities();
    }
}
