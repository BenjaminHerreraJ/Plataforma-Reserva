package com.hotel.room.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hotel.room.dto.HabitacionDTO;
import com.hotel.room.mapper.HabitacionMapper;
import com.hotel.room.model.Habitacion;
import com.hotel.room.repository.HabitacionRepository;

public class HabitacionServiceTest {
    
    @Mock
    private HabitacionRepository repo;

    @Mock
    private HabitacionMapper mapper;

    @InjectMocks
    private HabitacionService habitacionService;

    //CREAR HABITACION

    @Test
    @DisplayName("Debería crear una habitación exitosamente (Camino Feliz)")
    void crearHabitacionExitosamente() {
        // GIVEN
        HabitacionDTO requestDto = new HabitacionDTO();
        requestDto.setNumero("202");

        Habitacion habitacionPreSalvar = new Habitacion();
        habitacionPreSalvar.setNumero("202");

        Habitacion habitacionGuardada = new Habitacion();
        habitacionGuardada.setId(1L);
        habitacionGuardada.setNumero("202");

        HabitacionDTO responseDto = new HabitacionDTO();
        responseDto.setId(1L);
        responseDto.setNumero("202");

        when(repo.findByNumero("202")).thenReturn(Optional.empty());
        when(mapper.toEntity(requestDto)).thenReturn(habitacionPreSalvar);
        when(repo.save(habitacionPreSalvar)).thenReturn(habitacionGuardada);
        when(mapper.toDTO(habitacionGuardada)).thenReturn(responseDto);

        // WHEN
        HabitacionDTO resultado = habitacionService.crearHabitacion(requestDto);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("202", resultado.getNumero());
        verify(repo, times(1)).save(any(Habitacion.class));
    }

    @Test
    @DisplayName("Debería lanzar RuntimeException al intentar crear una habitación con número duplicado")
    void crearHabitacionNumeroDuplicado() {
        // GIVEN
        HabitacionDTO requestDto = new HabitacionDTO();
        requestDto.setNumero("202");

        Habitacion habitacionExistente = new Habitacion();

        when(repo.findByNumero("202")).thenReturn(Optional.of(habitacionExistente));

        // WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            habitacionService.crearHabitacion(requestDto);
        });

        assertEquals("El numero de habitacion ya esta registrado", exception.getMessage());
        verify(repo, never()).save(any(Habitacion.class));
    }

    //ACTUALIZAR HABITACION

    @Test
    @DisplayName("Debería actualizar una habitación exitosamente")
    void actualizarHabitacionExitosamente() {
        // GIVEN
        Long targetId = 1L;
        HabitacionDTO requestDto = new HabitacionDTO();
        requestDto.setNumero("303");
        requestDto.setTipo("Suite");
        requestDto.setPrecioNoche(90000.0);
        requestDto.setEstado("Disponible");

        Habitacion habitacionExistente = new Habitacion();
        habitacionExistente.setId(targetId);
        habitacionExistente.setNumero("101");

        Habitacion habitacionActualizada = new Habitacion();
        habitacionActualizada.setId(targetId);
        habitacionActualizada.setNumero("303");

        HabitacionDTO responseDto = new HabitacionDTO();
        responseDto.setId(targetId);
        responseDto.setNumero("303");

        when(repo.findById(targetId)).thenReturn(Optional.of(habitacionExistente));
        when(repo.findByNumero("303")).thenReturn(Optional.empty());
        when(repo.save(any(Habitacion.class))).thenReturn(habitacionActualizada);
        when(mapper.toDTO(habitacionActualizada)).thenReturn(responseDto);

        // WHEN
        HabitacionDTO resultado = habitacionService.actualizarHabitacion(targetId, requestDto);

        // THEN
        assertNotNull(resultado);
        assertEquals("303", resultado.getNumero());
        verify(repo, times(1)).save(habitacionExistente);
    }

    @Test
    @DisplayName("Debería lanzar RuntimeException al actualizar una habitación si el nuevo número ya lo usa otra")
    void actualizarHabitacionNumeroEnUsoPorOtra() {
        // GIVEN
        Long targetId = 1L;
        HabitacionDTO requestDto = new HabitacionDTO();
        requestDto.setNumero("505");

        Habitacion habitacionExistente = new Habitacion();
        habitacionExistente.setId(targetId);

        Habitacion otraHabitacionDiferente = new Habitacion();
        otraHabitacionDiferente.setId(99L); // ID distinto para gatillar el conflicto de negocio
        otraHabitacionDiferente.setNumero("505");

        when(repo.findById(targetId)).thenReturn(Optional.of(habitacionExistente));
        when(repo.findByNumero("505")).thenReturn(Optional.of(otraHabitacionDiferente));

        // WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            habitacionService.actualizarHabitacion(targetId, requestDto);
        });

        assertEquals("El número de habitación ya está en uso", exception.getMessage());
        verify(repo, never()).save(any(Habitacion.class));
    }

    //ELIMINAR HABITACION

    @Test
    @DisplayName("Debería eliminar una habitación si existe en la BD")
    void eliminarHabitacionExitosamente() {
        // GIVEN
        Long targetId = 2L;
        Habitacion habitacionExistente = new Habitacion();
        habitacionExistente.setId(targetId);

        when(repo.findById(targetId)).thenReturn(Optional.of(habitacionExistente));

        // WHEN
        habitacionService.eliminarHabitacion(targetId);

        // THEN
        verify(repo, times(1)).delete(habitacionExistente);
    }

    //CONSULTAS(LISTAR Y OBTENER POR ID)

    @Test
    @DisplayName("Debería listar todas las habitaciones registradas")
    void listarTodasLasHabitaciones() {
        // GIVEN
        Habitacion h = new Habitacion();
        when(repo.findAll()).thenReturn(List.of(h));
        when(mapper.toDTO(h)).thenReturn(new HabitacionDTO());

        // WHEN
        List<HabitacionDTO> resultado = habitacionService.listarTodas();

        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    @DisplayName("Debería obtener una habitación por ID exitosamente")
    void obtenerPorIdExitosamente() {
        // GIVEN
        Long targetId = 10L;
        Habitacion habitacion = new Habitacion();
        habitacion.setId(targetId);
        HabitacionDTO dto = new HabitacionDTO();
        dto.setId(targetId);

        when(repo.findById(targetId)).thenReturn(Optional.of(habitacion));
        when(mapper.toDTO(habitacion)).thenReturn(dto);

        // WHEN
        HabitacionDTO resultado = habitacionService.obtenerPorId(targetId);

        // THEN
        assertNotNull(resultado);
        assertEquals(targetId, resultado.getId());
    }

    @Test
    @DisplayName("Debería lanzar RuntimeException al buscar un ID que no existe")
    void obtenerPorIdNoEncontrado() {
        // GIVEN
        Long targetId = 404L;
        when(repo.findById(targetId)).thenReturn(Optional.empty());

        // WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            habitacionService.obtenerPorId(targetId);
        });

        assertEquals("Habitación no encontrada", exception.getMessage());
    }

}
