package com.hotel.room.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.room.dto.HabitacionDTO;
import com.hotel.room.service.HabitacionService;

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
@RequestMapping("/api/v1/habitaciones")
@Tag(name = "Controlador de Habitaciones", description = "Endpoints de ms-room para administrar el inventario y estado de las habitaciones del hotel")
public class HabitacionController {

    private final HabitacionService service;

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una habitación por su ID", description = "Busca en la base de datos distribuida los detalles de una habitación específica mediante su identificador único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Habitación encontrada exitosamente",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = HabitacionDTO.class)) }),
        @ApiResponse(responseCode = "404", description = "No se encontró ninguna habitación con el ID suministrado", content = @Content)
    })
    public ResponseEntity<HabitacionDTO> obtenerHabitacionPorId(@PathVariable Long id) {
        HabitacionDTO habitacion = service.obtenerPorId(id); 
        return new ResponseEntity<>(habitacion, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Registrar una nueva habitación", description = "Crea una habitación en el sistema asegurando que el número físico asignado no se encuentre duplicado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Habitación registrada correctamente",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = HabitacionDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Datos de entrada no válidos o número de habitación ya existente", content = @Content)
    })
    public ResponseEntity<HabitacionDTO> crearhabitacion(@Valid @RequestBody HabitacionDTO dto) {
        HabitacionDTO nuevaHabitacion = service.crearHabitacion(dto);
        return new ResponseEntity<>(nuevaHabitacion, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar todas las habitaciones", description = "Retorna el inventario completo de habitaciones sin aplicar filtros de estado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado de habitaciones obtenido con éxito",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = HabitacionDTO.class)) })
    })
    public ResponseEntity<List<HabitacionDTO>> listarHabitaciones() {
        List<HabitacionDTO> habitaciones = service.listarTodas();
        return new ResponseEntity<>(habitaciones, HttpStatus.OK);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar datos de una habitación existente", description = "Modifica propiedades como el tipo, precio o estado de ocupación. Valida que el nuevo número no choque con otra habitación.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Habitación modificada de forma exitosa",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = HabitacionDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Conflicto con los nuevos datos o número duplicado", content = @Content),
        @ApiResponse(responseCode = "404", description = "No se encontró la habitación correspondiente al ID", content = @Content)
    })
    public ResponseEntity<HabitacionDTO> actualizarHabitacion(@PathVariable Long id, @Valid @RequestBody HabitacionDTO dto) {
        HabitacionDTO habitacionActualizada = service.actualizarHabitacion(id, dto);
        return new ResponseEntity<>(habitacionActualizada, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una habitación del inventario", description = "Remueve físicamente el registro de la habitación si no cuenta con restricciones de integridad referencial.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Habitación eliminada con éxito (No Content)", content = @Content),
        @ApiResponse(responseCode = "404", description = "La habitación solicitada no existe", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno, posiblemente la habitación está vinculada a reservas vigentes", content = @Content)
    })
    public ResponseEntity<Void> eliminarHabitacion(@PathVariable Long id) {
        service.eliminarHabitacion(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    
}
