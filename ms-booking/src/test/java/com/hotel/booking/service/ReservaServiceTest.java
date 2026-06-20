package com.hotel.booking.service;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hotel.booking.client.HabitacionCliente;
import com.hotel.booking.client.HabitacionDTO;
import com.hotel.booking.client.HuespedCliente;
import com.hotel.booking.dto.ReservaDTO;
import com.hotel.booking.mapper.ReservaMapper;
import com.hotel.booking.model.Reserva;
import com.hotel.booking.repository.ReservaRepository;

import feign.FeignException;
@ExtendWith(MockitoExtension.class)
public class ReservaServiceTest {
    @Mock
    private ReservaRepository repository;

    @Mock
    private ReservaMapper mapper;

    @Mock
    private HabitacionCliente habitacionClient;

    @Mock
    private HuespedCliente huespedClient;

    @InjectMocks
    private ReservaService reservaService;

    @Test
    @DisplayName("Debería crear una reserva exitosamente calculando el monto correcto")
    void crearReservaExitosamente() {
        // 1. GIVEN
        ReservaDTO requestDto = new ReservaDTO();
        requestDto.setHuespedId(10L);
        requestDto.setHabitacionId(5L);
        requestDto.setFechaEntrada(LocalDate.of(2026, 10, 1));
        requestDto.setFechaSalida(LocalDate.of(2026, 10, 5)); // 4 noches de estadía

        HabitacionDTO habitacionSimulada = new HabitacionDTO();
        habitacionSimulada.setId(5L);
        habitacionSimulada.setEstado("Disponible");
        habitacionSimulada.setPrecioNoche(50000.0); // 4 noches * 50.000 = 200.000

        Reserva reservaGuardada = new Reserva();
        reservaGuardada.setId(100L);
        reservaGuardada.setMontoTotal(200000.0);
        reservaGuardada.setEstado("CONFIRMADA");

        ReservaDTO responseDto = new ReservaDTO();
        responseDto.setId(100L);
        responseDto.setMontoTotal(200000.0);
        responseDto.setEstado("CONFIRMADA");

        // Definimos comportamientos de los Mocks
        when(habitacionClient.obtenerHabitacionPorId(5L)).thenReturn(habitacionSimulada);
        // El cliente de huésped devuelve void, así que usamos doNothing() o simplemente no lo definimos
        
        when(repository.save(any(Reserva.class))).thenReturn(reservaGuardada);
        when(mapper.toDTO(reservaGuardada)).thenReturn(responseDto);

        // 2. WHEN
        ReservaDTO resultado = reservaService.crearReserva(requestDto);

        // 3. THEN
        assertNotNull(resultado);
        assertEquals(100L, resultado.getId());
        assertEquals(200000.0, resultado.getMontoTotal());
        assertEquals("CONFIRMADA", resultado.getEstado());

        // Verificaciones de comportamiento
        verify(habitacionClient, times(1)).obtenerHabitacionPorId(5L);
        verify(huespedClient, times(1)).obtenerHuespedPorId(10L);
        verify(repository, times(1)).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Debería lanzar IllegalArgumentException cuando la fecha de salida es igual o anterior a la de entrada")
    void crearReservaFechaInvalida() {
        // 1. GIVEN
        ReservaDTO requestDto = new ReservaDTO();
        requestDto.setFechaEntrada(LocalDate.of(2026, 10, 5));
        requestDto.setFechaSalida(LocalDate.of(2026, 10, 1)); // Error: salida antes que entrada

        // 2. WHEN & THEN
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reservaService.crearReserva(requestDto);
        });

        assertEquals("La fecha de salida debe ser posterior a la fecha de entrada", exception.getMessage());
        
        // Verificación de blindaje: Ningún cliente remoto ni repositorio debió ejecutarse
        verifyNoInteractions(habitacionClient, huespedClient, repository);
    }

    @Test
    @DisplayName("Debería lanzar IllegalStateException cuando la habitación está Ocupada o Mantenimiento")
    void crearReservaHabitacionNoDisponible() {
        // 1. GIVEN
        ReservaDTO requestDto = new ReservaDTO();
        requestDto.setHabitacionId(8L);
        requestDto.setFechaEntrada(LocalDate.of(2026, 10, 1));
        requestDto.setFechaSalida(LocalDate.of(2026, 10, 2));

        HabitacionDTO habitacionOcupada = new HabitacionDTO();
        habitacionOcupada.setId(8L);
        habitacionOcupada.setEstado("Ocupada"); // No está "Disponible"

        when(habitacionClient.obtenerHabitacionPorId(8L)).thenReturn(habitacionOcupada);

        // 2. WHEN & THEN
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            reservaService.crearReserva(requestDto);
        });

        assertEquals("La habitación no está disponible para reservar.", exception.getMessage());
        verify(repository, never()).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Debería lanzar RuntimeException con mensaje descriptivo cuando Feign lanza un 404 Not Found")
    void crearReservaFeignExceptionHabitacion() {
        // 1. GIVEN
        ReservaDTO requestDto = new ReservaDTO();
        requestDto.setHabitacionId(99L);
        requestDto.setFechaEntrada(LocalDate.of(2026, 10, 1));
        requestDto.setFechaSalida(LocalDate.of(2026, 10, 2));

        // Truco técnico para instanciar una excepción de Feign simulando un error 404
        FeignException.NotFound feignException = mock(FeignException.NotFound.class);
        
        when(habitacionClient.obtenerHabitacionPorId(99L)).thenThrow(feignException);

        // 2. WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reservaService.crearReserva(requestDto);
        });

        assertEquals("La habitación solicitada no existe en el sistema.", exception.getMessage());
        verify(huespedClient, never()).obtenerHuespedPorId(anyLong());
        verify(repository, never()).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Debería listar todas las reservas del historial transformadas a DTO")
    void listarTodasLasReservas() {
        // 1. GIVEN
        Reserva reserva = new Reserva();
        reserva.setId(1L);
        List<Reserva> listaSimulada = List.of(reserva);

        ReservaDTO dto = new ReservaDTO();
        dto.setId(1L);

        when(repository.findAll()).thenReturn(listaSimulada);
        when(mapper.toDTO(reserva)).thenReturn(dto);

        // 2. WHEN
        List<ReservaDTO> resultado = reservaService.listarTodas();

        // 3. THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
    }
}
