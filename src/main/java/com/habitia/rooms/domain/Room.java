package com.habitia.rooms.domain;

import com.habitia.shared.domain.valueobject.Money;
import com.habitia.shared.domain.valueobject.UserId;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Room {

    private final UUID id;
    private final UserId hostId;
    private String title;
    private String description;
    private String street;
    private String city;
    private String country;
    private Double latitude;
    private Double longitude;
    private Money price;
    private int maxGuests;
    private RoomStatus status;
    private final LocalDateTime createdAt;

    // Constructor para crear habitación nueva
    public Room(UserId hostId, String title, String description,
                String street, String city, String country,
                Double latitude, Double longitude,
                Money price, int maxGuests) {
        this.id = UUID.randomUUID();
        this.hostId = hostId;
        this.title = title;
        this.description = description;
        this.street = street;
        this.city = city;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.price = price;
        this.maxGuests = maxGuests;
        this.status = RoomStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
    }

    // Constructor para reconstruir desde persistencia
    public Room(UUID id, UserId hostId, String title, String description,
                String street, String city, String country,
                Double latitude, Double longitude,
                Money price, int maxGuests,
                RoomStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.hostId = hostId;
        this.title = title;
        this.description = description;
        this.street = street;
        this.city = city;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.price = price;
        this.maxGuests = maxGuests;
        this.status = status;
        this.createdAt = createdAt;
    }

    public void update(String title, String description, String street,
                       String city, String country, Money price, int maxGuests) {
        this.title = title;
        this.description = description;
        this.street = street;
        this.city = city;
        this.country = country;
        this.price = price;
        this.maxGuests = maxGuests;
    }

    public void deactivate() {
        this.status = RoomStatus.INACTIVE;
    }

    public void delete() {
        this.status = RoomStatus.DELETED;
    }

    public boolean isActive() {
        return this.status == RoomStatus.ACTIVE;
    }
}