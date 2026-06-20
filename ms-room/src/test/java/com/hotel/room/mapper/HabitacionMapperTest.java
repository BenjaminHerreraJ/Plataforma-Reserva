package com.hotel.room.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.hotel.room.dto.HabitacionDTO;
import com.hotel.room.model.Habitacion;

public class HabitacionMapperTest {

    private final HabitacionMapper mapper = new HabitacionMapper();

    @Test
    @DisplayName("Debería mapear de Entidad Habitacion a DTO correctamente")
    void toDTOSuccess() {
        // GIVEN
        Habitacion entidad = new Habitacion();
        entidad.setId(10L);
        entidad.setNumero("305");
        entidad.setTipo("Deluxe");
        entidad.setPrecioNoche(85000.0);
        entidad.setEstado("Disponible");

        // WHEN
        HabitacionDTO dto = mapper.toDTO(entidad);

        // THEN
        assertNotNull(dto);
        assertEquals(entidad.getId(), dto.getId());
        assertEquals(entidad.getNumero(), dto.getNumero());
        assertEquals(entidad.getTipo(), dto.getTipo());
        assertEquals(entidad.getPrecioNoche(), dto.getPrecioNoche());
        assertEquals(entidad.getEstado(), dto.getEstado());
    }

    @Test
    @DisplayName("Debería retornar null si la Entidad Habitacion es null")
    void toDTONull() {
        // WHEN & THEN
        assertNull(mapper.toDTO(null));
    }

    @Test
    @DisplayName("Debería mapear de DTO Habitacion a Entidad correctamente")
    void toEntitySuccess() {
        // GIVEN
        HabitacionDTO dto = new HabitacionDTO();
        dto.setId(20L);
        dto.setNumero("401");
        dto.setTipo("Simple");
        dto.setPrecioNoche(45000.0);
        dto.setEstado("Ocupada");

        // WHEN
        Habitacion entidad = mapper.toEntity(dto);

        // THEN
        assertNotNull(entidad);
        assertEquals(dto.getId(), entidad.getId());
        assertEquals(dto.getNumero(), entidad.getNumero());
        assertEquals(dto.getTipo(), entidad.getTipo());
        assertEquals(dto.getPrecioNoche(), entidad.getPrecioNoche());
        assertEquals(dto.getEstado(), entidad.getEstado());
    }

    @Test
    @DisplayName("Debería retornar null si el DTO Habitacion es null")
    void toEntityNull() {
        // WHEN & THEN
        assertNull(mapper.toEntity(null));
    }

}
