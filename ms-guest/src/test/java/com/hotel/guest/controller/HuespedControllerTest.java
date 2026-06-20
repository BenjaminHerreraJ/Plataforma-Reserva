package com.hotel.guest.controller;
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
import com.hotel.guest.dto.HuespedDTO;
import com.hotel.guest.service.HuespedService;

@WebMvcTest(HuespedController.class)
public class HuespedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HuespedService huespedService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/v1/huespedes - Debería registrar un huésped y retornar status 201")
    void whenCrearHuesped_thenReturnCreated() throws Exception {
        // GIVEN
        HuespedDTO requestDto = new HuespedDTO();
        requestDto.setRutPasaporte("12345678-9");
        requestDto.setNombre("Esteban Dido");

        HuespedDTO responseDto = new HuespedDTO();
        responseDto.setId(1L);
        responseDto.setRutPasaporte("12345678-9");
        responseDto.setNombre("Esteban Dido");

        when(huespedService.registrarHuesped(any(HuespedDTO.class))).thenReturn(responseDto);

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/huespedes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.rutPasaporte").value("12345678-9"))
                .andExpect(jsonPath("$.nombre").value("Esteban Dido"));
    }

    @Test
    @DisplayName("GET /api/v1/huespedes - Debería retornar listado completo de huéspedes y status 200")
    void whenListarHuespedes_thenReturnOk() throws Exception {
        // GIVEN
        List<HuespedDTO> lista = List.of(new HuespedDTO());
        when(huespedService.listarTodos()).thenReturn(lista);

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/huespedes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("GET /api/v1/huespedes/{id} - Debería retornar el huésped por ID y status 200")
    void whenObtenerHuespedPorId_thenReturnOk() throws Exception {
        // GIVEN
        Long targetId = 1L;
        HuespedDTO dto = new HuespedDTO();
        dto.setId(targetId);
        dto.setRutPasaporte("98765432-1");

        when(huespedService.buscarPorId(targetId)).thenReturn(dto);

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/huespedes/{id}", targetId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(targetId))
                .andExpect(jsonPath("$.rutPasaporte").value("98765432-1"));
    }

}
