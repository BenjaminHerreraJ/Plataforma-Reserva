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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/huespedes")
public class HuespedController {
    private final HuespedService service;

    @PostMapping
    public ResponseEntity<HuespedDTO> crear(@Valid @RequestBody HuespedDTO dto) {
        return new ResponseEntity<>(service.registrarHuesped(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<HuespedDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HuespedDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }
}
