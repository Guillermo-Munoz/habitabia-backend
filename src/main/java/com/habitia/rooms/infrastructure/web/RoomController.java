package com.habitia.rooms.infrastructure.web;

import com.habitia.rooms.application.*;
import com.habitia.reviews.application.GetRoomRatingUseCase;
import com.habitia.bookings.application.GetBookedDatesUseCase;
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
import com.habitia.rooms.infrastructure.web.BookedDateRangeResponse;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    private final PublishRoomUseCase publishRoomUseCase;
    private final SearchRoomsUseCase searchRoomsUseCase;
    private final GetRoomUseCase getRoomUseCase;
    private final UploadRoomImageUseCase uploadRoomImageUseCase;
    private final GetAvailableCitiesUseCase getAvailableCitiesUseCase;
    private final GetAvailableRoomsByDatesUseCase getAvailableRoomsByDatesUseCase;
    private final GetRoomRatingUseCase getRoomRatingUseCase;
    private final GetBookedDatesUseCase getBookedDatesUseCase;

    public RoomController(PublishRoomUseCase publishRoomUseCase,
                          SearchRoomsUseCase searchRoomsUseCase,
                          GetRoomUseCase getRoomUseCase,
                          UploadRoomImageUseCase uploadRoomImageUseCase,
                          GetAvailableCitiesUseCase getAvailableCitiesUseCase,
                          GetAvailableRoomsByDatesUseCase getAvailableRoomsByDatesUseCase,
                          GetRoomRatingUseCase getRoomRatingUseCase,
                          GetBookedDatesUseCase getBookedDatesUseCase) {
        this.publishRoomUseCase = publishRoomUseCase;
        this.searchRoomsUseCase = searchRoomsUseCase;
        this.getRoomUseCase = getRoomUseCase;
        this.uploadRoomImageUseCase = uploadRoomImageUseCase;
        this.getAvailableCitiesUseCase = getAvailableCitiesUseCase;
        this.getAvailableRoomsByDatesUseCase = getAvailableRoomsByDatesUseCase;
        this.getRoomRatingUseCase = getRoomRatingUseCase;
        this.getBookedDatesUseCase = getBookedDatesUseCase;
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
                request.maxGuests(),
                request.amenities()
        ));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RoomResponse.from(room, getRoomRatingUseCase.execute(room.getId())));
    }

    @GetMapping
    public ResponseEntity<List<RoomResponse>> search(
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "1") int guests) {
        var rooms = searchRoomsUseCase.execute(city, guests);
        return ResponseEntity.ok(rooms.stream()
                .map(room -> RoomResponse.from(room, getRoomRatingUseCase.execute(room.getId())))
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getById(@PathVariable UUID id) {
        var room = getRoomUseCase.execute(id);
        return ResponseEntity.ok(RoomResponse.from(room, getRoomRatingUseCase.execute(id)));
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
                        .stream()
                        .map(room -> RoomResponse.from(room, getRoomRatingUseCase.execute(room.getId())))
                        .toList()
        );
    }

    @GetMapping("/{id}/booked-dates")
    public ResponseEntity<List<BookedDateRangeResponse>> getBookedDates(@PathVariable UUID id) {
        return ResponseEntity.ok(
                getBookedDatesUseCase.execute(id)
                        .stream()
                        .map(b -> new BookedDateRangeResponse(b.getDateRange().checkIn(), b.getDateRange().checkOut()))
                        .toList()
        );
    }

    @PostMapping("/{id}/images")
    public ResponseEntity<RoomResponse> uploadImage(
            @PathVariable UUID id,
            @RequestParam MultipartFile file,
            Authentication auth) {
        var room = uploadRoomImageUseCase.execute(id, auth.getName(), file);
        return ResponseEntity.ok(RoomResponse.from(room, getRoomRatingUseCase.execute(id)));
    }
}