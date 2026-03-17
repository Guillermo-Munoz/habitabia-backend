package com.habitia.rooms.infrastructure.persistence;

import com.habitia.rooms.domain.Room;
import com.habitia.rooms.domain.RoomRepository;
import com.habitia.rooms.domain.RoomStatus;
import com.habitia.shared.domain.valueobject.Money;
import com.habitia.shared.domain.valueobject.UserId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RoomRepositoryAdapter implements RoomRepository {

    private final RoomJpaRepository jpaRepository;

    public RoomRepositoryAdapter(RoomJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Room save(Room room) {
        return toDomain(jpaRepository.save(toEntity(room)));
    }

    @Override
    public Optional<Room> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Room> findByHostId(UserId hostId) {
        return jpaRepository.findByHostId(hostId.value())
                .stream().map(this::toDomain).toList();
    }

    @Override
    public List<Room> searchAvailable(String city, int guests) {
        return jpaRepository.searchAvailable(city, guests)
                .stream().map(this::toDomain).toList();
    }

    private RoomJpaEntity toEntity(Room room) {
        RoomJpaEntity e = new RoomJpaEntity();
        e.setId(room.getId());
        e.setHostId(room.getHostId().value());
        e.setTitle(room.getTitle());
        e.setDescription(room.getDescription());
        e.setStreet(room.getStreet());
        e.setCity(room.getCity());
        e.setCountry(room.getCountry());
        e.setLatitude(room.getLatitude());
        e.setLongitude(room.getLongitude());
        e.setPriceAmount(room.getPrice().amount());
        e.setPriceCurrency(room.getPrice().currency());
        e.setMaxGuests(room.getMaxGuests());
        e.setStatus(room.getStatus().name());
        e.setCreatedAt(room.getCreatedAt());
        return e;
    }

    private Room toDomain(RoomJpaEntity e) {
        return new Room(
                e.getId(),
                new UserId(e.getHostId()),
                e.getTitle(),
                e.getDescription(),
                e.getStreet(),
                e.getCity(),
                e.getCountry(),
                e.getLatitude(),
                e.getLongitude(),
                Money.of(e.getPriceAmount(), e.getPriceCurrency()),
                e.getMaxGuests(),
                RoomStatus.valueOf(e.getStatus()),
                e.getCreatedAt()
        );
    }
}