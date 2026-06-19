package com.hotel.guest.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.hotel.guest.dto.HuespedDTO;
import com.hotel.guest.model.Huesped;

public class HuespedMapperTest {

    private final HuespedMapper mapper = new HuespedMapper();

    @Test
    @DisplayName("Debería mapear de Entidad a DTO correctamente")
    void toDTOSuccess() {
        // GIVEN
        Huesped entidad = new Huesped();
        entidad.setId(1L);
        entidad.setRutPasaporte("12345678-9");
        entidad.setNombre("Juan Pérez");
        entidad.setEmail("juan@correo.com");
        entidad.setTelefono("+56912345678");

        // WHEN
        HuespedDTO dto = mapper.toDTO(entidad);

        // THEN
        assertNotNull(dto);
        assertEquals(entidad.getId(), dto.getId());
        assertEquals(entidad.getRutPasaporte(), dto.getRutPasaporte());
        assertEquals(entidad.getNombre(), dto.getNombre());
        assertEquals(entidad.getEmail(), dto.getEmail());
        assertEquals(entidad.getTelefono(), dto.getTelefono());
    }

    @Test
    @DisplayName("Debería retornar null si la Entidad es null al mapear a DTO")
    void toDTONull() {
        // WHEN & THEN
        assertNull(mapper.toDTO(null));
    }

    @Test
    @DisplayName("Debería mapear de DTO a Entidad correctamente")
    void toEntitySuccess() {
        // GIVEN
        HuespedDTO dto = new HuespedDTO();
        dto.setId(2L);
        dto.setRutPasaporte("98765432-1");
        dto.setNombre("María López");
        dto.setEmail("maria@correo.com");
        dto.setTelefono("+56987654321");

        // WHEN
        Huesped entidad = mapper.toEntity(dto);

        // THEN
        assertNotNull(entidad);
        assertEquals(dto.getId(), entidad.getId());
        assertEquals(dto.getRutPasaporte(), entidad.getRutPasaporte());
        assertEquals(dto.getNombre(), entidad.getNombre());
        assertEquals(dto.getEmail(), entidad.getEmail());
        assertEquals(dto.getTelefono(), entidad.getTelefono());
    }

    @Test
    @DisplayName("Debería retornar null si el DTO es null al mapear a Entidad")
    void toEntityNull() {
        // WHEN & THEN
        assertNull(mapper.toEntity(null));
    }

}
