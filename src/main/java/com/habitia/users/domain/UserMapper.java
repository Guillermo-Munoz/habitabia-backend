package com.habitia.users.domain;

import com.habitia.shared.domain.valueobject.UserId;
import com.habitia.users.infrastructure.persistence.UserJpaEntity;

public class UserMapper {

    public UserJpaEntity toEntity(User user, String newPassword) {
        UserJpaEntity entity = new UserJpaEntity();

        entity.setId(user.getId().value());
        entity.setFullName(user.getFullName());
        entity.setEmail(user.getEmail());

        if (newPassword != null && !newPassword.isBlank()) {
            entity.setPassword(newPassword);
        } else {

            entity.setPassword(user.getPassword());
        }

        entity.setRole(user.getRole().name());
        entity.setBio(user.getBio());
        entity.setAvatarUrl(user.getAvatarUrl());
        entity.setActive(user.isActive());
        entity.setCreatedAt(user.getCreatedAt());

        return entity;
    }

    public User toDomain(UserJpaEntity entity) {
        return new User(
                new UserId(entity.getId()),
                entity.getFullName(),
                entity.getEmail(),
                entity.getPassword(),
                UserRole.valueOf(entity.getRole()),
                entity.getBio(),
                entity.getAvatarUrl(),
                entity.isActive(),
                entity.getCreatedAt()
        );
    }

    public UserJpaEntity toEntity(User user) {
        return toEntity(user, null);
    }
}