package com.hotel.guest.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hotel.guest.dto.HuespedDTO;
import com.hotel.guest.mapper.HuespedMapper;
import com.hotel.guest.model.Huesped;
import com.hotel.guest.repository.HuespedRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class HuespedService {

    private final HuespedRepository repo;
    private final HuespedMapper mapper;

    public HuespedDTO registrarHuesped(HuespedDTO dto) {
        log.info("Registrando nuevo huesped con RUT: {}", dto.getRutPasaporte());

        if(repo.findByRutPasaporte(dto.getRutPasaporte()).isPresent()){
            log.error("Error: El RUT {} ya está registrado", dto.getRutPasaporte());
            throw new RuntimeException("El huésped ya existe en el sistema");
        }
        Huesped nuevoH = mapper.toEntity(dto);
        return mapper.toDTO(repo.save(nuevoH));
    }

    public List<HuespedDTO> listarTodos() {
        log.info("Obteniendo lista de todos los huéspedes");
        return repo.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    
}
