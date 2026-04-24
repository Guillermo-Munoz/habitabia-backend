package com.habitia.rooms.infrastructure.web;

import com.habitia.rooms.application.*;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    private final PublishRoomUseCase publishRoomUseCase;
    private final SearchRoomsUseCase searchRoomsUseCase;
    private final GetRoomUseCase getRoomUseCase;
    private final UploadRoomImageUseCase uploadRoomImageUseCase;
    private final GetAvailableCitiesUseCase getAvailableCitiesUseCase;
    private final GetAvailableRoomsByDatesUseCase getAvailableRoomsByDatesUseCase;

    public RoomController(PublishRoomUseCase publishRoomUseCase,
                          SearchRoomsUseCase searchRoomsUseCase,
                          GetRoomUseCase getRoomUseCase,
                          UploadRoomImageUseCase uploadRoomImageUseCase,
                          GetAvailableCitiesUseCase getAvailableCitiesUseCase,
                          GetAvailableRoomsByDatesUseCase getAvailableRoomsByDatesUseCase) {
        this.publishRoomUseCase = publishRoomUseCase;
        this.searchRoomsUseCase = searchRoomsUseCase;
        this.getRoomUseCase = getRoomUseCase;
        this.uploadRoomImageUseCase = uploadRoomImageUseCase;
        this.getAvailableCitiesUseCase = getAvailableCitiesUseCase;
        this.getAvailableRoomsByDatesUseCase = getAvailableRoomsByDatesUseCase;
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
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "1") int guests) {
        var rooms = searchRoomsUseCase.execute(city, guests);
        return ResponseEntity.ok(rooms.stream().map(RoomResponse::from).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(RoomResponse.from(getRoomUseCase.execute(id)));
    }

    @GetMapping("/cities")
    public ResponseEntity<List<String>> getCities() {
        return ResponseEntity.ok(getAvailableCitiesUseCase.execute());
    }

    @GetMapping("/available")
    public ResponseEntity<List<RoomResponse>> getAvailableByDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            @RequestParam(defaultValue = "1") int guests) {
        return ResponseEntity.ok(
                getAvailableRoomsByDatesUseCase.execute(checkIn, checkOut, guests)
                        .stream().map(RoomResponse::from).toList()
        );
    }

    @PostMapping("/{id}/images")
    public ResponseEntity<RoomResponse> uploadImage(
            @PathVariable UUID id,
            @RequestParam("file") MultipartFile file,
            Authentication auth) {
        var room = uploadRoomImageUseCase.execute(id, auth.getName(), file);
        return ResponseEntity.ok(RoomResponse.from(room));
    }
}