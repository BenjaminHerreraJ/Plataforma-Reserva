package com.hotel.guest.service;

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

import com.hotel.guest.dto.HuespedDTO;
import com.hotel.guest.mapper.HuespedMapper;
import com.hotel.guest.model.Huesped;
import com.hotel.guest.repository.HuespedRepository;

public class HuespedServiceTest {

    @Mock
    private HuespedRepository repo;

    @Mock
    private HuespedMapper mapper;

    @InjectMocks
    private HuespedService huespedService;

    
    // REGISTRAR HUÉSPED
    

    @Test
    @DisplayName("Debería registrar un huésped exitosamente (Camino Feliz)")
    void registrarHuespedExitosamente() {
        // GIVEN
        HuespedDTO requestDto = new HuespedDTO();
        requestDto.setRutPasaporte("12345678-9");

        Huesped huespedPreSalvar = new Huesped();
        huespedPreSalvar.setRutPasaporte("12345678-9");

        Huesped huespedGuardado = new Huesped();
        huespedGuardado.setId(1L);
        huespedGuardado.setRutPasaporte("12345678-9");

        HuespedDTO responseDto = new HuespedDTO();
        responseDto.setId(1L);
        responseDto.setRutPasaporte("12345678-9");

        when(repo.findByRutPasaporte("12345678-9")).thenReturn(Optional.empty());
        when(mapper.toEntity(requestDto)).thenReturn(huespedPreSalvar);
        when(repo.save(huespedPreSalvar)).thenReturn(huespedGuardado);
        when(mapper.toDTO(huespedGuardado)).thenReturn(responseDto);

        // WHEN
        HuespedDTO resultado = huespedService.registrarHuesped(requestDto);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("12345678-9", resultado.getRutPasaporte());
        verify(repo, times(1)).save(any(Huesped.class));
    }

    @Test
    @DisplayName("Debería lanzar RuntimeException si el RUT ya se encuentra registrado")
    void registrarHuespedRutDuplicado() {
        // GIVEN
        HuespedDTO requestDto = new HuespedDTO();
        requestDto.setRutPasaporte("12345678-9");

        Huesped huespedExistente = new Huesped();

        when(repo.findByRutPasaporte("12345678-9")).thenReturn(Optional.of(huespedExistente));

        // WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            huespedService.registrarHuesped(requestDto);
        });

        assertEquals("El huésped ya existe en el sistema", exception.getMessage());
        verify(repo, never()).save(any(Huesped.class));
    }

    
    // BUSCAR POR ID
    

    @Test
    @DisplayName("Debería buscar y retornar un huésped por su ID exitosamente")
    void buscarPorIdExitosamente() {
        // GIVEN
        Long targetId = 5L;
        Huesped huesped = new Huesped();
        huesped.setId(targetId);
        HuespedDTO dto = new HuespedDTO();
        dto.setId(targetId);

        when(repo.findById(targetId)).thenReturn(Optional.of(huesped));
        when(mapper.toDTO(huesped)).thenReturn(dto);

        // WHEN
        HuespedDTO resultado = huespedService.buscarPorId(targetId);

        // THEN
        assertNotNull(resultado);
        assertEquals(targetId, resultado.getId());
    }

    @Test
    @DisplayName("Debería lanzar RuntimeException cuando el ID del huésped no existe")
    void buscarPorIdNoEncontrado() {
        // GIVEN
        Long targetId = 99L;
        when(repo.findById(targetId)).thenReturn(Optional.empty());

        // WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            huespedService.buscarPorId(targetId);
        });

        assertEquals("Huésped no encontrado", exception.getMessage());
    }

    
    // LISTAR TODOS
    

    @Test
    @DisplayName("Debería obtener la lista completa de todos los huéspedes")
    void listarTodosLosHuespedes() {
        // GIVEN
        Huesped huesped = new Huesped();
        when(repo.findAll()).thenReturn(List.of(huesped));
        when(mapper.toDTO(huesped)).thenReturn(new HuespedDTO());

        // WHEN
        List<HuespedDTO> resultado = huespedService.listarTodos();

        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(repo, times(1)).findAll();
    }

}
