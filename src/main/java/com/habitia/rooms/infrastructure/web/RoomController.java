package com.habitia.rooms.infrastructure.web;

import com.habitia.rooms.application.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    private final PublishRoomUseCase publishRoomUseCase;
    private final SearchRoomsUseCase searchRoomsUseCase;
    private final GetRoomUseCase getRoomUseCase;

    public RoomController(PublishRoomUseCase publishRoomUseCase,
                          SearchRoomsUseCase searchRoomsUseCase,
                          GetRoomUseCase getRoomUseCase) {
        this.publishRoomUseCase = publishRoomUseCase;
        this.searchRoomsUseCase = searchRoomsUseCase;
        this.getRoomUseCase = getRoomUseCase;
    }

    @PostMapping
    public ResponseEntity<RoomResponse> publish(
            @Valid @RequestBody PublishRoomRequest request,
            Authentication auth) {
        var room = publishRoomUseCase.execute(new PublishRoomCommand(
                auth.getName(),
                request.title(),
                request.description(),
                request.street(),
                request.city(),
                request.country(),
                request.latitude(),
                request.longitude(),
                request.priceAmount(),
                request.priceCurrency(),
                request.maxGuests()
        ));
        return ResponseEntity.status(HttpStatus.CREATED).body(RoomResponse.from(room));
    }

    @GetMapping
    public ResponseEntity<List<RoomResponse>> search(
            @RequestParam String city,
            @RequestParam(defaultValue = "1") int guests) {
        var rooms = searchRoomsUseCase.execute(city, guests);
        return ResponseEntity.ok(rooms.stream().map(RoomResponse::from).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(RoomResponse.from(getRoomUseCase.execute(id)));
    }
}