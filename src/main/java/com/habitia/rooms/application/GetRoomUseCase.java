package com.habitia.rooms.application;

import com.habitia.rooms.domain.Room;
import com.habitia.rooms.domain.RoomRepository;
import com.habitia.shared.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetRoomUseCase {

    private final RoomRepository roomRepository;

    public GetRoomUseCase(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room execute(UUID id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room", id.toString()));
    }
}