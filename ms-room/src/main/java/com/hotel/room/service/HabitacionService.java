package com.hotel.room.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hotel.room.dto.HabitacionDTO;
import com.hotel.room.mapper.HabitacionMapper;
import com.hotel.room.model.Habitacion;
import com.hotel.room.repository.HabitacionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class HabitacionService {

    private final HabitacionRepository repo;

    private final HabitacionMapper mapper;

    public HabitacionDTO crearHabitacion(HabitacionDTO dto){
        log.info("Iniciando creación de habitacion número: {}", dto.getNumero());

        if(repo.findByNumero(dto.getNumero()).isPresent()){
            log.error("Fallo al crear: La habitacion {} ya existe", dto.getNumero());
            throw new RuntimeException("El numero de habitacion ya esta registrado");
        }

        try{
            Habitacion habitacion = mapper.toEntity(dto);

            Habitacion guardada = repo.save(habitacion);
            log.info("Habitacion {} creada con éxito con ID: {}", guardada.getNumero(), guardada.getId());
            return mapper.toDTO(guardada);
        } catch (Exception e) {
            log.error("Error inesperado al guardar la habitación: {}", e.getMessage());
            throw new RuntimeException("Error interno al procesar la solicitud");
        }

    }

    public List<HabitacionDTO> listarTodas(){
        log.info("Consultando todas las habitaciones");
        return repo.findAll().stream()
            .map(mapper::toDTO)
            .collect(Collectors.toList());
    }

    // METODO ACTUALIZAR (PUT)
    public HabitacionDTO actualizarHabitacion(Long id, HabitacionDTO dto) {
        log.info("Iniciando actualización de la habitación con ID: {}", id);

        // BUSCAR SI LA HABITACION EXISTE
        Habitacion habitacionExistente = repo.findById(id)
                .orElseThrow(() -> {
                    log.error("Error al actualizar: No se encontró la habitación con ID {}", id);
                    return new RuntimeException("Habitación no encontrada");
                });

        // VERIFICAR QUE EL NUEVO NUMERO DE HABITACION NO PERTENEZCA A OTRA YA EXISTENTE
        repo.findByNumero(dto.getNumero()).ifPresent(h -> {
            if (!h.getId().equals(id)) {
                log.error("Fallo al actualizar: El número {} ya está en uso por otra habitación", dto.getNumero());
                throw new RuntimeException("El número de habitación ya está en uso");
            }
        });

        try {
            // SE ACTUALIZAN LOS DATOS (MENOS EL ID)
            habitacionExistente.setNumero(dto.getNumero());
            habitacionExistente.setTipo(dto.getTipo());
            habitacionExistente.setPrecioNoche(dto.getPrecioNoche());
            habitacionExistente.setEstado(dto.getEstado());

            // GUARADMOS Y RETORNAMOS
            Habitacion actualizada = repo.save(habitacionExistente);
            log.info("Habitación ID: {} actualizada con éxito", id);
            return mapper.toDTO(actualizada);

        } catch (Exception e) {
            log.error("Error inesperado al actualizar la habitación ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error interno al actualizar la habitación");
        }
    }

    // METODO BORRAR (DELETE)
    public void eliminarHabitacion(Long id) {
        log.info("Iniciando eliminación de la habitación con ID: {}", id);

        Habitacion habitacionExistente = repo.findById(id)
                .orElseThrow(() -> {
                    log.error("Error al eliminar: No se encontró la habitación con ID {}", id);
                    return new RuntimeException("Habitación no encontrada");
                });

        try {
            repo.delete(habitacionExistente);
            log.info("Habitación ID: {} eliminada con éxito", id);
        } catch (Exception e) {
            log.error("Error al eliminar la habitación ID {}: {}", id, e.getMessage());
            throw new RuntimeException("No se pudo eliminar la habitación. Puede que tenga dependencias.");
        }
    }
}
