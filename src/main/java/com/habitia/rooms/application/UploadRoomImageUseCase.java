package com.habitia.rooms.application;

import com.habitia.rooms.domain.Room;
import com.habitia.rooms.domain.RoomRepository;
import com.habitia.shared.domain.exception.BusinessRuleException;
import com.habitia.shared.domain.exception.ResourceNotFoundException;
import com.habitia.shared.domain.storage.StorageService;
import com.habitia.shared.domain.valueobject.UserId;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class UploadRoomImageUseCase {

    private final RoomRepository roomRepository;
    private final StorageService storageService;

    public UploadRoomImageUseCase(RoomRepository roomRepository, StorageService storageService) {
        this.roomRepository = roomRepository;
        this.storageService = storageService;
    }

    public Room execute(UUID roomId, String hostId, MultipartFile file) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room", roomId.toString()));

        if (!room.getHostId().equals(UserId.of(hostId))) {
            throw new BusinessRuleException("Solo el anfitrión puede subir imágenes a esta habitación");
        }

        String imageUrl = storageService.store(file);
        room.addImage(imageUrl);
        return roomRepository.save(room);
    }
}
