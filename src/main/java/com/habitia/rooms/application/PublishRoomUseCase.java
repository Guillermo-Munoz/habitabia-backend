package com.habitia.rooms.application;

import com.habitia.rooms.domain.Room;
import com.habitia.rooms.domain.RoomRepository;
import com.habitia.shared.domain.valueobject.Money;
import com.habitia.shared.domain.valueobject.UserId;
import org.springframework.stereotype.Service;

@Service
public class PublishRoomUseCase {

    private final RoomRepository roomRepository;

    public PublishRoomUseCase(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room execute(PublishRoomCommand command) {
        Room room = new Room(
                UserId.of(command.hostId()),
                command.title(),
                command.description(),
                command.street(),
                command.city(),
                command.country(),
                command.latitude(),
                command.longitude(),
                Money.of(command.priceAmount(), command.priceCurrency()),
                command.maxGuests()
        );
        return roomRepository.save(room);
    }
}