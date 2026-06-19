package com.hotel.booking.mapper;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.hotel.booking.dto.ReservaDTO;
import com.hotel.booking.model.Reserva;

public class ReservaMapperTest {

    private final ReservaMapper mapper = new ReservaMapper();

    @Test
    @DisplayName("Debería mapear de Entidad Reserva a DTO correctamente")
    void toDTOSuccess() {
        // GIVEN
        Reserva entidad = new Reserva();
        entidad.setId(1L);
        entidad.setHabitacionId(101L);
        entidad.setHuespedId(5L);
        entidad.setFechaEntrada(LocalDate.of(2026, 6, 20));
        entidad.setFechaSalida(LocalDate.of(2026, 6, 25));
        entidad.setMontoTotal(250000.0);
        entidad.setEstado("CONFIRMADA");

        // WHEN
        ReservaDTO dto = mapper.toDTO(entidad);

        // THEN
        assertNotNull(dto);
        assertEquals(entidad.getId(), dto.getId());
        assertEquals(entidad.getHabitacionId(), dto.getHabitacionId());
        assertEquals(entidad.getHuespedId(), dto.getHuespedId());
        assertEquals(entidad.getFechaEntrada(), dto.getFechaEntrada());
        assertEquals(entidad.getFechaSalida(), dto.getFechaSalida());
        assertEquals(entidad.getMontoTotal(), dto.getMontoTotal());
        assertEquals(entidad.getEstado(), dto.getEstado());
    }

    @Test
    @DisplayName("Debería retornar null si la Entidad Reserva es null")
    void toDTONull() {
        // WHEN & THEN
        assertNull(mapper.toDTO(null));
    }

}
