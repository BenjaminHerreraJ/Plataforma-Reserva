package com.hotel.room.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.room.dto.HabitacionDTO;
import com.hotel.room.service.HabitacionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/habitaciones")
public class HabitacionController {

    private final HabitacionService service;

    @GetMapping("/{id}")
    public ResponseEntity<HabitacionDTO> obtenerHabitacionPorId(@PathVariable Long id) {
        // Asegúrate de que tu HabitacionService tenga este método implementado (puede llamarse obtenerPorId o buscarPorId)
        HabitacionDTO habitacion = service.obtenerPorId(id); 
        return new ResponseEntity<>(habitacion, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HabitacionDTO> crearhabitacion(@Valid @RequestBody HabitacionDTO dto) {
        HabitacionDTO nuevaHabitacion = service.crearHabitacion(dto);
        return new ResponseEntity<>(nuevaHabitacion, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<HabitacionDTO>> listarHabitaciones() {
        List<HabitacionDTO> habitaciones = service.listarTodas();
        return new ResponseEntity<>(habitaciones, HttpStatus.OK);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<HabitacionDTO> actualizarHabitacion(@PathVariable Long id, @Valid @RequestBody HabitacionDTO dto) {
        
        HabitacionDTO habitacionActualizada = service.actualizarHabitacion(id, dto);
        return new ResponseEntity<>(habitacionActualizada, HttpStatus.OK);
    }

    
}
