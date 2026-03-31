package com.habitia.rooms.domain;

import com.habitia.shared.domain.valueobject.Money;
import com.habitia.shared.domain.valueobject.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    private final UserId hostId = UserId.generate();
    private final Money precio = Money.euro(new BigDecimal("75.00"));

    // -------------------------------------------------------------------------
    // Constructor habitación nueva
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Crear habitación nueva debería asignar estado ACTIVE por defecto")
    void crearRoom_deberiaAsignarEstadoActivePorDefecto() {
        Room room = new Room(hostId, "Piso céntrico", "Descripción", "Calle Mayor 1",
                "Madrid", "España", 40.4168, -3.7038, precio, 2);

        assertEquals(RoomStatus.ACTIVE, room.getStatus());
        assertTrue(room.isActive());
    }

    @Test
    @DisplayName("Crear habitación nueva debería generar un id no nulo")
    void crearRoom_deberiaGenerarIdNoNulo() {
        Room room = new Room(hostId, "Piso céntrico", "Descripción", "Calle Mayor 1",
                "Madrid", "España", 40.4168, -3.7038, precio, 2);

        assertNotNull(room.getId());
    }

    @Test
    @DisplayName("Crear habitación nueva debería asignar createdAt no nulo")
    void crearRoom_deberiaAsignarCreatedAtNoNulo() {
        Room room = new Room(hostId, "Piso céntrico", "Descripción", "Calle Mayor 1",
                "Madrid", "España", 40.4168, -3.7038, precio, 2);

        assertNotNull(room.getCreatedAt());
    }

    @Test
    @DisplayName("Crear habitación nueva debería guardar todos los campos correctamente")
    void crearRoom_deberiaGuardarTodosLosCampos() {
        Room room = new Room(hostId, "Piso céntrico", "Bonito piso", "Calle Mayor 1",
                "Madrid", "España", 40.4168, -3.7038, precio, 3);

        assertEquals(hostId, room.getHostId());
        assertEquals("Piso céntrico", room.getTitle());
        assertEquals("Bonito piso", room.getDescription());
        assertEquals("Calle Mayor 1", room.getStreet());
        assertEquals("Madrid", room.getCity());
        assertEquals("España", room.getCountry());
        assertEquals(40.4168, room.getLatitude());
        assertEquals(-3.7038, room.getLongitude());
        assertEquals(precio, room.getPrice());
        assertEquals(3, room.getMaxGuests());
    }

    // -------------------------------------------------------------------------
    // Constructor de reconstrucción desde persistencia
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Reconstruir habitación desde persistencia debería mantener todos los datos")
    void reconstruirRoom_deberiaMantenerTodosLosDatos() {
        UUID id = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();

        Room room = new Room(id, hostId, "Piso céntrico", "Descripción", "Calle Mayor 1",
                "Madrid", "España", 40.4168, -3.7038, precio, 2,
                RoomStatus.INACTIVE, createdAt, null);

        assertEquals(id, room.getId());
        assertEquals(hostId, room.getHostId());
        assertEquals("Piso céntrico", room.getTitle());
        assertEquals("Descripción", room.getDescription());
        assertEquals("Calle Mayor 1", room.getStreet());
        assertEquals("Madrid", room.getCity());
        assertEquals("España", room.getCountry());
        assertEquals(40.4168, room.getLatitude());
        assertEquals(-3.7038, room.getLongitude());
        assertEquals(precio, room.getPrice());
        assertEquals(2, room.getMaxGuests());
        assertEquals(RoomStatus.INACTIVE, room.getStatus());
        assertEquals(createdAt, room.getCreatedAt());
    }

    // -------------------------------------------------------------------------
    // update
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Actualizar habitación debería modificar los campos editables")
    void actualizar_deberiaActualizarLosCampos() {
        Room room = new Room(hostId, "Título original", "Desc original", "Calle 1",
                "Madrid", "España", 40.4168, -3.7038, precio, 2);
        Money nuevoPrecio = Money.euro(new BigDecimal("100.00"));

        room.update("Nuevo título", "Nueva descripción", "Calle Nueva 5",
                "Barcelona", "España", nuevoPrecio, 4);

        assertEquals("Nuevo título", room.getTitle());
        assertEquals("Nueva descripción", room.getDescription());
        assertEquals("Calle Nueva 5", room.getStreet());
        assertEquals("Barcelona", room.getCity());
        assertEquals("España", room.getCountry());
        assertEquals(nuevoPrecio, room.getPrice());
        assertEquals(4, room.getMaxGuests());
    }

    @Test
    @DisplayName("Actualizar habitación no debería cambiar el estado ni el id")
    void actualizar_noDeberiaModificarEstadoNiId() {
        Room room = new Room(hostId, "Título", "Desc", "Calle 1",
                "Madrid", "España", 40.4168, -3.7038, precio, 2);
        UUID idOriginal = room.getId();

        room.update("Nuevo título", "Nueva desc", "Calle 2",
                "Sevilla", "España", precio, 3);

        assertEquals(idOriginal, room.getId());
        assertEquals(RoomStatus.ACTIVE, room.getStatus());
    }

    // -------------------------------------------------------------------------
    // deactivate / delete / isActive
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Desactivar habitación debería poner el estado a INACTIVE")
    void desactivar_deberiaCambiarEstadoAInactive() {
        Room room = new Room(hostId, "Piso", "Desc", "Calle 1",
                "Madrid", "España", 40.4168, -3.7038, precio, 2);

        room.deactivate();

        assertEquals(RoomStatus.INACTIVE, room.getStatus());
        assertFalse(room.isActive());
    }

    @Test
    @DisplayName("Eliminar habitación debería poner el estado a DELETED")
    void eliminar_deberiaCambiarEstadoADeleted() {
        Room room = new Room(hostId, "Piso", "Desc", "Calle 1",
                "Madrid", "España", 40.4168, -3.7038, precio, 2);

        room.delete();

        assertEquals(RoomStatus.DELETED, room.getStatus());
        assertFalse(room.isActive());
    }

    @Test
    @DisplayName("isActive devuelve true solo cuando el estado es ACTIVE")
    void isActive_conEstadoActive_deberiaRetornarTrue() {
        Room room = new Room(hostId, "Piso", "Desc", "Calle 1",
                "Madrid", "España", 40.4168, -3.7038, precio, 2);

        assertTrue(room.isActive());
    }

    @Test
    @DisplayName("isActive devuelve false cuando el estado es INACTIVE")
    void isActive_conEstadoInactive_deberiaRetornarFalse() {
        Room room = new Room(UUID.randomUUID(), hostId, "Piso", "Desc", "Calle 1",
                "Madrid", "España", 40.4168, -3.7038, precio, 2,
                RoomStatus.INACTIVE, LocalDateTime.now(), null);

        assertFalse(room.isActive());
    }

    @Test
    @DisplayName("isActive devuelve false cuando el estado es DELETED")
    void isActive_conEstadoDeleted_deberiaRetornarFalse() {
        Room room = new Room(UUID.randomUUID(), hostId, "Piso", "Desc", "Calle 1",
                "Madrid", "España", 40.4168, -3.7038, precio, 2,
                RoomStatus.DELETED, LocalDateTime.now(), null);

        assertFalse(room.isActive());
    }
}
