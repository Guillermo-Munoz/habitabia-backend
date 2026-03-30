package com.habitia.bookings.domain;

import com.habitia.shared.domain.exception.BusinessRuleException;
import com.habitia.shared.domain.valueobject.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {

    private final UUID roomId = UUID.randomUUID();
    private final UserId guestId = UserId.generate();
    private final UserId hostId = UserId.generate();
    private final LocalDate checkIn = LocalDate.now().plusDays(1);
    private final LocalDate checkOut = LocalDate.now().plusDays(5);

    // -------------------------------------------------------------------------
    // Constructor nueva reserva
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Crear reserva nueva debería asignar estado REQUESTED por defecto")
    void crearBooking_deberiaAsignarEstadoRequestedPorDefecto() {
        Booking booking = new Booking(roomId, guestId, hostId, checkIn, checkOut, 2, "Hola");

        assertEquals(BookingStatus.REQUESTED, booking.getStatus());
    }

    @Test
    @DisplayName("Crear reserva nueva debería generar un id no nulo")
    void crearBooking_deberiaGenerarIdNoNulo() {
        Booking booking = new Booking(roomId, guestId, hostId, checkIn, checkOut, 2, "Hola");

        assertNotNull(booking.getId());
    }

    @Test
    @DisplayName("Crear reserva nueva debería asignar createdAt no nulo")
    void crearBooking_deberiaAsignarCreatedAtNoNulo() {
        Booking booking = new Booking(roomId, guestId, hostId, checkIn, checkOut, 2, "Hola");

        assertNotNull(booking.getCreatedAt());
    }

    @Test
    @DisplayName("Crear reserva nueva debería guardar todos los campos correctamente")
    void crearBooking_deberiaGuardarTodosLosCampos() {
        Booking booking = new Booking(roomId, guestId, hostId, checkIn, checkOut, 2, "Hola, me interesa");

        assertEquals(roomId, booking.getRoomId());
        assertEquals(guestId, booking.getGuestId());
        assertEquals(hostId, booking.getHostId());
        assertEquals(checkIn, booking.getDateRange().checkIn());
        assertEquals(checkOut, booking.getDateRange().checkOut());
        assertEquals(2, booking.getGuests());
        assertEquals("Hola, me interesa", booking.getMessage());
    }

    // -------------------------------------------------------------------------
    // Constructor de reconstrucción desde persistencia
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Reconstruir reserva desde persistencia debería mantener todos los datos")
    void reconstruirBooking_deberiaMantenerTodosLosDatos() {
        UUID id = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();

        Booking booking = new Booking(id, roomId, guestId, hostId, checkIn, checkOut,
                3, BookingStatus.ACCEPTED, "Mensaje", createdAt);

        assertEquals(id, booking.getId());
        assertEquals(roomId, booking.getRoomId());
        assertEquals(guestId, booking.getGuestId());
        assertEquals(hostId, booking.getHostId());
        assertEquals(checkIn, booking.getDateRange().checkIn());
        assertEquals(checkOut, booking.getDateRange().checkOut());
        assertEquals(3, booking.getGuests());
        assertEquals(BookingStatus.ACCEPTED, booking.getStatus());
        assertEquals("Mensaje", booking.getMessage());
        assertEquals(createdAt, booking.getCreatedAt());
    }

    // -------------------------------------------------------------------------
    // accept
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Aceptar reserva REQUESTED debería cambiar estado a ACCEPTED")
    void aceptar_conEstadoRequested_deberiaCambiarAAccepted() {
        Booking booking = new Booking(roomId, guestId, hostId, checkIn, checkOut, 2, "Hola");

        booking.accept();

        assertEquals(BookingStatus.ACCEPTED, booking.getStatus());
    }

    @Test
    @DisplayName("Aceptar reserva no REQUESTED debería lanzar excepción")
    void aceptar_conEstadoNoRequested_deberiaLanzarExcepcion() {
        Booking booking = new Booking(roomId, guestId, hostId, checkIn, checkOut, 2, "Hola");
        booking.accept();

        assertThrows(BusinessRuleException.class, booking::accept);
    }

    // -------------------------------------------------------------------------
    // reject
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Rechazar reserva REQUESTED debería cambiar estado a CANCELLED")
    void rechazar_conEstadoRequested_deberiaCambiarACancelled() {
        Booking booking = new Booking(roomId, guestId, hostId, checkIn, checkOut, 2, "Hola");

        booking.reject();

        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
    }

    @Test
    @DisplayName("Rechazar reserva no REQUESTED debería lanzar excepción")
    void rechazar_conEstadoNoRequested_deberiaLanzarExcepcion() {
        Booking booking = new Booking(roomId, guestId, hostId, checkIn, checkOut, 2, "Hola");
        booking.accept();

        assertThrows(BusinessRuleException.class, booking::reject);
    }

    // -------------------------------------------------------------------------
    // confirm
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Confirmar reserva ACCEPTED debería cambiar estado a CONFIRMED")
    void confirmar_conEstadoAccepted_deberiaCambiarAConfirmed() {
        Booking booking = new Booking(roomId, guestId, hostId, checkIn, checkOut, 2, "Hola");
        booking.accept();

        booking.confirm();

        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
    }

    @Test
    @DisplayName("Confirmar reserva no ACCEPTED debería lanzar excepción")
    void confirmar_conEstadoNoAccepted_deberiaLanzarExcepcion() {
        Booking booking = new Booking(roomId, guestId, hostId, checkIn, checkOut, 2, "Hola");

        assertThrows(BusinessRuleException.class, booking::confirm);
    }

    // -------------------------------------------------------------------------
    // cancel
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Cancelar reserva REQUESTED debería cambiar estado a CANCELLED")
    void cancelar_conEstadoRequested_deberiaCambiarACancelled() {
        Booking booking = new Booking(roomId, guestId, hostId, checkIn, checkOut, 2, "Hola");

        booking.cancel();

        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
    }

    @Test
    @DisplayName("Cancelar reserva ACCEPTED debería cambiar estado a CANCELLED")
    void cancelar_conEstadoAccepted_deberiaCambiarACancelled() {
        Booking booking = new Booking(roomId, guestId, hostId, checkIn, checkOut, 2, "Hola");
        booking.accept();

        booking.cancel();

        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
    }

    @Test
    @DisplayName("Cancelar reserva CONFIRMED debería cambiar estado a CANCELLED")
    void cancelar_conEstadoConfirmed_deberiaCambiarACancelled() {
        Booking booking = new Booking(roomId, guestId, hostId, checkIn, checkOut, 2, "Hola");
        booking.accept();
        booking.confirm();

        booking.cancel();

        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
    }

    @Test
    @DisplayName("Cancelar reserva ya CANCELLED debería lanzar excepción")
    void cancelar_conEstadoCancelled_deberiaLanzarExcepcion() {
        Booking booking = new Booking(roomId, guestId, hostId, checkIn, checkOut, 2, "Hola");
        booking.cancel();

        assertThrows(BusinessRuleException.class, booking::cancel);
    }

    @Test
    @DisplayName("Cancelar reserva COMPLETED debería lanzar excepción")
    void cancelar_conEstadoCompleted_deberiaLanzarExcepcion() {
        Booking booking = new Booking(roomId, guestId, hostId, checkIn, checkOut, 2, "Hola");
        booking.accept();
        booking.confirm();
        booking.complete();

        assertThrows(BusinessRuleException.class, booking::cancel);
    }

    // -------------------------------------------------------------------------
    // complete
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Completar reserva CONFIRMED debería cambiar estado a COMPLETED")
    void completar_conEstadoConfirmed_deberiaCambiarACompleted() {
        Booking booking = new Booking(roomId, guestId, hostId, checkIn, checkOut, 2, "Hola");
        booking.accept();
        booking.confirm();

        booking.complete();

        assertEquals(BookingStatus.COMPLETED, booking.getStatus());
    }

    @Test
    @DisplayName("Completar reserva no CONFIRMED debería lanzar excepción")
    void completar_conEstadoNoConfirmed_deberiaLanzarExcepcion() {
        Booking booking = new Booking(roomId, guestId, hostId, checkIn, checkOut, 2, "Hola");
        booking.accept();

        assertThrows(BusinessRuleException.class, booking::complete);
    }
}
