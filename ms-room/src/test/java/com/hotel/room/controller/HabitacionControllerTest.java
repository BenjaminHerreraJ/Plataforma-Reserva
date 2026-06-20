package com.hotel.room.controller;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.room.dto.HabitacionDTO;
import com.hotel.room.service.HabitacionService;

@WebMvcTest(HabitacionController.class)
public class HabitacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HabitacionService habitacionService;

    @Autowired
    private ObjectMapper objectMapper; // Nos ayuda a transformar objetos de Java a JSON string

    @Test
    @DisplayName("GET /api/v1/habitaciones - Debería retornar listado con un elemento y status 200")
    void whenListarHabitaciones_thenReturnOk() throws Exception {
        // GIVEN
        List<HabitacionDTO> lista = List.of(new HabitacionDTO());

        // WHEN
        when(habitacionService.listarTodas()).thenReturn(lista);

        // THEN
        mockMvc.perform(get("/api/v1/habitaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("GET /api/v1/habitaciones/{id} - Debería retornar la habitación solicitada y status 200")
    void whenObtenerHabitacionPorId_thenReturnOk() throws Exception {
        // GIVEN
        Long targetId = 1L;
        HabitacionDTO dto = new HabitacionDTO();
        dto.setId(targetId);
        dto.setNumero("101");

        // WHEN
        when(habitacionService.obtenerPorId(targetId)).thenReturn(dto);

        // THEN
        mockMvc.perform(get("/api/v1/habitaciones/{id}", targetId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(targetId))
                .andExpect(jsonPath("$.numero").value("101"));
    }

    @Test
    @DisplayName("POST /api/v1/habitaciones - Debería crear una habitación y retornar status 201")
    void whenCrearHabitacion_thenReturnCreated() throws Exception {
        // GIVEN
        HabitacionDTO requestDto = new HabitacionDTO();
        requestDto.setNumero("202");
        requestDto.setTipo("Suite");

        HabitacionDTO responseDto = new HabitacionDTO();
        responseDto.setId(1L);
        responseDto.setNumero("202");

        // Mapeamos el comportamiento del service
        when(habitacionService.crearHabitacion(any(HabitacionDTO.class))).thenReturn(responseDto);

        // THEN
        mockMvc.perform(post("/api/v1/habitaciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))) // Convierte el DTO a JSON
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.numero").value("202"));
    }

    @Test
    @DisplayName("PUT /api/v1/habitaciones/{id} - Debería actualizar y retornar status 200")
    void whenActualizarHabitacion_thenReturnOk() throws Exception {
        // GIVEN
        Long targetId = 1L;
        HabitacionDTO requestDto = new HabitacionDTO();
        requestDto.setNumero("101-B");

        HabitacionDTO responseDto = new HabitacionDTO();
        responseDto.setId(targetId);
        responseDto.setNumero("101-B");

        when(habitacionService.actualizarHabitacion(eq(targetId), any(HabitacionDTO.class))).thenReturn(responseDto);

        // THEN
        mockMvc.perform(put("/api/v1/habitaciones/{id}", targetId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numero").value("101-B"));
    }

    @Test
    @DisplayName("DELETE /api/v1/habitaciones/{id} - Debería eliminar la habitación y retornar status 204")
    void whenEliminarHabitacion_thenReturnNoContent() throws Exception {
        // GIVEN
        Long targetId = 1L;

        // THEN
        mockMvc.perform(delete("/api/v1/habitaciones/{id}", targetId))
                .andExpect(status().isNoContent());
    }

}
