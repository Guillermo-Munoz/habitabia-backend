package com.habitia.rooms.domain;

import com.habitia.shared.domain.valueobject.Money;
import com.habitia.shared.domain.valueobject.UserId;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private final List<String> imageUrls;

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
        this.imageUrls = new ArrayList<>();
    }

    // Constructor para reconstruir desde persistencia
    public Room(UUID id, UserId hostId, String title, String description,
                String street, String city, String country,
                Double latitude, Double longitude,
                Money price, int maxGuests,
                RoomStatus status, LocalDateTime createdAt, List<String> imageUrls) {
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
        this.imageUrls = imageUrls != null ? new ArrayList<>(imageUrls) : new ArrayList<>();

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

    public void addImage(String imageUrl){
        this.imageUrls.add(imageUrl);
    }

    public void removeImage(String imageUrl){
        this.imageUrls.remove(imageUrl);
    }

    public List<String> getImageUrls() {
        return new ArrayList<>(imageUrls);
    }
}