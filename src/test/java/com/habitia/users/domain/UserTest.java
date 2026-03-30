package com.habitia.users.domain;

import com.habitia.shared.domain.valueobject.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    // -------------------------------------------------------------------------
    // Constructor usuario normal
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Crear usuario normal con rol USER por defecto")
    void crearUsuarioNormal_deberiaAsignarRolUserPorDefecto() {
        User user = new User("Juan García", "juan@email.com", "password123");

        assertEquals("Juan García", user.getFullName());
        assertEquals("juan@email.com", user.getEmail());
        assertEquals(UserRole.USER, user.getRole());
        assertTrue(user.isActive());
        assertNotNull(user.getId());
        assertNotNull(user.getCreatedAt());
    }

    @Test
    @DisplayName("El email se guarda en minúsculas y sin espacios")
    void crearUsuario_deberiaGuardarEmailNormalizado() {
        User user = new User("Juan García", "  JUAN@EMAIL.COM  ", "password123");

        assertEquals("juan@email.com", user.getEmail());
    }

    @Test
    @DisplayName("El nombre se guarda sin espacios al inicio y al final")
    void crearUsuario_deberiaGuardarNombreTrimado() {
        User user = new User("  Juan García  ", "juan@email.com", "password123");

        assertEquals("Juan García", user.getFullName());
    }

    @Test
    @DisplayName("Lanza excepción si el nombre está vacío")
    void crearUsuario_conNombreVacio_deberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                new User("", "juan@email.com", "password123")
        );
    }

    @Test
    @DisplayName("Lanza excepción si el nombre supera 100 caracteres")
    void crearUsuario_conNombreMuyLargo_deberiaLanzarExcepcion() {
        String nombreLargo = "a".repeat(101);
        assertThrows(IllegalArgumentException.class, () ->
                new User(nombreLargo, "juan@email.com", "password123")
        );
    }

    @Test
    @DisplayName("Lanza excepción si el email tiene formato inválido")
    void crearUsuario_conEmailInvalido_deberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                new User("Juan García", "esto-no-es-un-email", "password123")
        );
    }

    @Test
    @DisplayName("Lanza excepción si el email está vacío")
    void crearUsuario_conEmailVacio_deberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                new User("Juan García", "", "password123")
        );
    }

    @Test
    @DisplayName("Lanza excepción si el email supera 100 caracteres")
    void crearUsuario_conEmailMuyLargo_deberiaLanzarExcepcion() {
        String emailLargo = "a".repeat(95) + "@email.com";
        assertThrows(IllegalArgumentException.class, () ->
                new User("Juan García", emailLargo, "password123")
        );
    }

    // -------------------------------------------------------------------------
    // Constructor admin
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Crear admin con rol ADMIN explícito")
    void crearAdmin_deberiaAsignarRolAdmin() {
        User admin = new User("Admin", "admin@email.com", "password123", UserRole.ADMIN);

        assertEquals(UserRole.ADMIN, admin.getRole());
        assertTrue(admin.isAdmin());
    }

    @Test
    @DisplayName("Lanza excepción si se usa el constructor admin con rol USER")
    void crearAdmin_conRolUser_deberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                new User("Admin", "admin@email.com", "password123", UserRole.USER)
        );
    }

    // -------------------------------------------------------------------------
    // Constructor de reconstrucción desde persistencia
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Reconstruir usuario desde persistencia")
    void reconstruirUsuario_deberiaManetenerTodosLosDatos() {
        UserId id = UserId.generate();
        LocalDateTime createdAt = LocalDateTime.now();

        User user = new User(id, "Juan García", "juan@email.com", "hashedPassword",
                UserRole.USER, "Mi bio", "avatar.jpg", true, createdAt);

        assertEquals(id, user.getId());
        assertEquals("Juan García", user.getFullName());
        assertEquals("juan@email.com", user.getEmail());
        assertEquals(UserRole.USER, user.getRole());
        assertEquals("Mi bio", user.getBio());
        assertEquals("avatar.jpg", user.getAvatarUrl());
        assertTrue(user.isActive());
        assertEquals(createdAt, user.getCreatedAt());
    }

    @Test
    @DisplayName("Si el rol es null al reconstruir, se asigna USER por defecto")
    void reconstruirUsuario_conRolNull_deberiaAsignarUserPorDefecto() {
        User user = new User(UserId.generate(), "Juan", "juan@email.com", "pass",
                null, null, null, true, LocalDateTime.now());

        assertEquals(UserRole.USER, user.getRole());
    }

    // -------------------------------------------------------------------------
    // Métodos de negocio
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Actualizar perfil correctamente")
    void actualizarPerfil_deberiaActualizarLosCampos() {
        User user = new User("Juan García", "juan@email.com", "password123");

        user.updateProfile("Juan Nuevo", "Mi nueva bio", "nueva-foto.jpg");

        assertEquals("Juan Nuevo", user.getFullName());
        assertEquals("Mi nueva bio", user.getBio());
        assertEquals("nueva-foto.jpg", user.getAvatarUrl());
    }

    @Test
    @DisplayName("Actualizar perfil con bio y avatar null no lanza excepción")
    void actualizarPerfil_conBioYAvatarNull_noDeberiaLanzarExcepcion() {
        User user = new User("Juan García", "juan@email.com", "password123");

        assertDoesNotThrow(() -> user.updateProfile("Juan García", null, null));
        assertNull(user.getBio());
        assertNull(user.getAvatarUrl());
    }

    @Test
    @DisplayName("Cambiar contraseña correctamente")
    void cambiarPassword_deberiaActualizarLaPassword() {
        User user = new User("Juan García", "juan@email.com", "password123");
        String nuevaPassword = encoder.encode("nuevaPassword");

        user.changePassword(nuevaPassword);

        assertTrue(user.passwordMatches("nuevaPassword", encoder));
    }

    @Test
    @DisplayName("Lanza excepción si la nueva contraseña está vacía")
    void cambiarPassword_conPasswordVacia_deberiaLanzarExcepcion() {
        User user = new User("Juan García", "juan@email.com", "password123");

        assertThrows(IllegalArgumentException.class, () ->
                user.changePassword("")
        );
    }

    @Test
    @DisplayName("Lanza excepción si la nueva contraseña es null")
    void cambiarPassword_conPasswordNull_deberiaLanzarExcepcion() {
        User user = new User("Juan García", "juan@email.com", "password123");

        assertThrows(IllegalArgumentException.class, () ->
                user.changePassword(null)
        );
    }

    @Test
    @DisplayName("passwordMatches devuelve true con la contraseña correcta")
    void passwordMatches_conPasswordCorrecta_deberiaRetornarTrue() {
        String rawPassword = "miPassword123";
        String hashed = encoder.encode(rawPassword);
        User user = new User(UserId.generate(), "Juan", "juan@email.com", hashed,
                UserRole.USER, null, null, true, LocalDateTime.now());

        assertTrue(user.passwordMatches(rawPassword, encoder));
    }

    @Test
    @DisplayName("passwordMatches devuelve false con contraseña incorrecta")
    void passwordMatches_conPasswordIncorrecta_deberiaRetornarFalse() {
        String hashed = encoder.encode("passwordCorrecta");
        User user = new User(UserId.generate(), "Juan", "juan@email.com", hashed,
                UserRole.USER, null, null, true, LocalDateTime.now());

        assertFalse(user.passwordMatches("passwordIncorrecta", encoder));
    }

    // -------------------------------------------------------------------------
    // Activar / Desactivar
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Desactivar usuario")
    void desactivar_deberiaPonerActivoAFalse() {
        User user = new User("Juan García", "juan@email.com", "password123");

        user.deactivate();

        assertFalse(user.isActive());
    }

    @Test
    @DisplayName("Activar usuario previamente desactivado")
    void activar_deberiaPonerActivoATrue() {
        User user = new User("Juan García", "juan@email.com", "password123");
        user.deactivate();

        user.activate();

        assertTrue(user.isActive());
    }

    // -------------------------------------------------------------------------
    // isAdmin
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("isAdmin devuelve false para usuario normal")
    void isAdmin_conRolUser_deberiaRetornarFalse() {
        User user = new User("Juan García", "juan@email.com", "password123");

        assertFalse(user.isAdmin());
    }

    @Test
    @DisplayName("isAdmin devuelve true para admin")
    void isAdmin_conRolAdmin_deberiaRetornarTrue() {
        User admin = new User("Admin", "admin@email.com", "password123", UserRole.ADMIN);

        assertTrue(admin.isAdmin());
    }
}