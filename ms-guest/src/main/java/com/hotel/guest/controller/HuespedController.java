package com.hotel.guest.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.guest.dto.HuespedDTO;
import com.hotel.guest.service.HuespedService;

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
@RequestMapping("/api/v1/huespedes")
@Tag(name = "Controlador de Huéspedes", description = "Endpoints de ms-guest para el registro, consulta y gestión de los huéspedes del hotel")
public class HuespedController {
    
    private final HuespedService service;

    @PostMapping
    @Operation(summary = "Registrar un nuevo huésped", description = "Crea un registro de huésped en el sistema validando que el RUT o pasaporte proporcionado no exista previamente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Huésped registrado exitosamente",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = HuespedDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Argumentos de entrada inválidos o RUT ya registrado en el sistema", content = @Content)
    })
    public ResponseEntity<HuespedDTO> crear(@Valid @RequestBody HuespedDTO dto) {
        return new ResponseEntity<>(service.registrarHuesped(dto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar todos los huéspedes", description = "Obtiene una lista completa con todos los clientes/huéspedes almacenados en la base de datos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado de huéspedes recuperado con éxito",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = HuespedDTO.class)) })
    })
    public ResponseEntity<List<HuespedDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar un huésped por ID", description = "Retorna la información detallada de un huésped específico utilizando su identificador único del sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Huésped encontrado de forma exitosa",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = HuespedDTO.class)) }),
        @ApiResponse(responseCode = "404", description = "No existe un huésped con el ID solicitado en los registros", content = @Content)
    })
    public ResponseEntity<HuespedDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }
}
