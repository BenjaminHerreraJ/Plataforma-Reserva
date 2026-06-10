package com.hotel.booking.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.booking.dto.ReservaDTO;
import com.hotel.booking.service.ReservaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservas")
@Tag(name = "Controlador de Reservas", description = "Endpoints de ms-booking para la gestión e historial de las reservas del hotel")
public class ReservaController {

    private final ReservaService service;

    @PostMapping
    @Operation(
        summary = "Crear una nueva reserva de habitación", 
        description = "Registra una reserva calculando automáticamente el costo total basado en los días de estadía. Verifica disponibilidad de habitación y existencia del huésped llamando a los otros microservicios mediante Feign."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Reserva generada con éxito en la base de datos remota",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ReservaDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Error de validación o fechas de entrada/salida inconsistentes", content = @Content),
        @ApiResponse(responseCode = "404", description = "La habitación o el huésped no existen en el sistema distribuido", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor o falla en la comunicación del ecosistema", content = @Content)
    })
    public ResponseEntity<ReservaDTO> crearReserva(@Valid @RequestBody ReservaDTO dto) {
        ReservaDTO nuevaReserva = service.crearReserva(dto);
        return new ResponseEntity<>(nuevaReserva, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(
        summary = "Obtener historial de reservas", 
        description = "Retorna una lista con todas las reservas registradas en el sistema mapeadas a formato DTO."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado de reservas obtenido exitosamente",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ReservaDTO.class)) })
    })
    public ResponseEntity<List<ReservaDTO>> listarReservas() {
        List<ReservaDTO> reservas = service.listarTodas(); // Nota: Tenías 'service.service', asegúrate de que sea 'service.listarTodas()'
        return new ResponseEntity<>(reservas, HttpStatus.OK);
    }
}
