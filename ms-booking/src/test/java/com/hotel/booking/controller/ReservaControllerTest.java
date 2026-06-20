package com.hotel.booking.controller;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.booking.dto.ReservaDTO;
import com.hotel.booking.service.ReservaService;

@WebMvcTest(ReservaController.class)
public class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservaService reservaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/v1/reservas - Debería crear una reserva exitosamente y retornar status 201")
    void whenCrearReserva_thenReturnCreated() throws Exception {
        // GIVEN
        ReservaDTO requestDto = new ReservaDTO();
        requestDto.setHabitacionId(101L);
        requestDto.setHuespedId(5L);
        requestDto.setFechaEntrada(LocalDate.of(2026, 6, 20));
        requestDto.setFechaSalida(LocalDate.of(2026, 6, 25));

        ReservaDTO responseDto = new ReservaDTO();
        responseDto.setId(123L);
        responseDto.setHabitacionId(101L);
        responseDto.setEstado("CONFIRMADA");
        responseDto.setMontoTotal(250000.0);

        when(reservaService.crearReserva(any(ReservaDTO.class))).thenReturn(responseDto);

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(123L))
                .andExpect(jsonPath("$.estado").value("CONFIRMADA"))
                .andExpect(jsonPath("$.montoTotal").value(250000.0));
    }

    @Test
    @DisplayName("GET /api/v1/reservas - Debería retornar todas las reservas y status 200")
    void whenListarReservas_thenReturnOk() throws Exception {
        // GIVEN
        List<ReservaDTO> lista = List.of(new ReservaDTO());
        when(reservaService.listarTodas()).thenReturn(lista);

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/reservas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

}
