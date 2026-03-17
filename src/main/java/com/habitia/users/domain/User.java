package com.habitia.users.domain;

import com.habitia.shared.domain.valueobject.UserId;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Getter
public class User {
    private final UserId id;
    private String fullName;
    private String email;

    @Getter(AccessLevel.PACKAGE)  // ⚠️ Solo visible en el mismo paquete
    private String password;

    private UserRole role;
    private String bio;
    private String avatarUrl;
    private boolean active;
    private final LocalDateTime createdAt;

    // Constructor para usuario normal (rol USER por defecto)
    public User(String fullName, String email, String password) {
        validateFullName(fullName);
        validateEmail(email);
        this.id = UserId.generate();
        this.fullName = fullName.trim();
        this.email = email.trim().toLowerCase();
        this.password = password;
        this.role = UserRole.USER;
        this.active = true;
        this.createdAt = LocalDateTime.now();
    }

    // Constructor para admin (rol explícito)
    public User(String fullName, String email, String password, UserRole role) {
        if (role != UserRole.ADMIN) {
            throw new IllegalArgumentException("Solo se puede crear ADMIN con este constructor");
        }
        validateFullName(fullName);
        validateEmail(email);
        this.id = UserId.generate();
        this.fullName = fullName.trim();
        this.email = email.trim().toLowerCase();
        this.password = password;
        this.role = role;
        this.active = true;
        this.createdAt = LocalDateTime.now();
    }

    // Constructor para reconstruir desde persistencia
    public User(UserId id, String fullName, String email, String password,
                UserRole role, String bio, String avatarUrl,
                boolean active, LocalDateTime createdAt) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role != null ? role : UserRole.USER;
        this.bio = bio;
        this.avatarUrl = avatarUrl;
        this.active = active;
        this.createdAt = createdAt;
    }

    // Métodos de negocio
    public void updateProfile(String fullName, String bio, String avatarUrl) {
        validateFullName(fullName);
        this.fullName = fullName.trim();
        this.bio = bio != null ? bio.trim() : null;
        this.avatarUrl = avatarUrl != null ? avatarUrl.trim() : null;
    }

    public void changePassword(String newHashedPassword) {
        if (newHashedPassword == null || newHashedPassword.isBlank()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }
        this.password = newHashedPassword;
    }

    public boolean passwordMatches(String rawPassword, PasswordEncoder encoder) {
        return encoder.matches(rawPassword, this.password);
    }

    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

    public boolean isAdmin() {
        return this.role == UserRole.ADMIN;
    }

    // Validaciones privadas
    private void validateFullName(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (fullName.length() > 100) {
            throw new IllegalArgumentException("El nombre es demasiado largo (máx. 100 caracteres)");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Formato de email inválido");
        }
        if (email.length() > 100) {
            throw new IllegalArgumentException("El email es demasiado largo");
        }
    }
}