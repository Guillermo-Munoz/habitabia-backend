package com.habitia.users.domain;

import com.habitia.shared.domain.valueobject.UserId;

import java.util.Optional;

public interface UserRepository {
    // Guardar usuario normal
    User save(User user);

    // Guardar usuario con nueva contraseña (para cambios de password)
    User save(User user, String newHashedPassword);

    // Buscar por ID
    Optional<User> findById(UserId id);

    // Buscar por email
    Optional<User> findByEmail(String email);

    // Verificar si existe un email
    boolean existsByEmail(String email);
}