package com.hotel.booking.service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hotel.booking.client.HabitacionCliente;
import com.hotel.booking.client.HabitacionDTO;
import com.hotel.booking.client.HuespedCliente;
import com.hotel.booking.dto.ReservaDTO;
import com.hotel.booking.mapper.ReservaMapper;
import com.hotel.booking.model.Reserva;
import com.hotel.booking.repository.ReservaRepository;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservaService {
    private final ReservaRepository repository;
    private final ReservaMapper mapper;
    private final HabitacionCliente habitacionClient;
    private final HuespedCliente huespedClient;

    public ReservaDTO crearReserva(ReservaDTO dto) {
        log.info("Iniciando proceso de reserva para huésped ID: {} en habitación ID: {}", dto.getHuespedId(), dto.getHabitacionId());

        if (dto.getFechaSalida().isBefore(dto.getFechaEntrada()) || dto.getFechaSalida().isEqual(dto.getFechaEntrada())) {
            log.error("Fecha de salida inválida");
            throw new IllegalArgumentException("La fecha de salida debe ser posterior a la fecha de entrada");
        }

        HabitacionDTO habitacion;
        try {
            log.info("Consultando MS Habitaciones mediante Feign...");
            habitacion = habitacionClient.obtenerHabitacionPorId(dto.getHabitacionId());
            if (!habitacion.getEstado().equalsIgnoreCase("Disponible")) {
                throw new IllegalStateException("La habitación no está disponible para reservar.");
            }
        } catch (FeignException.NotFound e) {
            log.error("Habitación no encontrada en el servicio remoto");
            throw new RuntimeException("La habitación solicitada no existe en el sistema.");
        }

        try {
            log.info("Consultando MS Huéspedes mediante Feign...");
            huespedClient.obtenerHuespedPorId(dto.getHuespedId());
        } catch (FeignException.NotFound e) {
            log.error("Huésped no encontrado en el servicio remoto");
            throw new RuntimeException("El huésped no está registrado en el sistema.");
        }

        long diasEstadia = ChronoUnit.DAYS.between(dto.getFechaEntrada(), dto.getFechaSalida());
        Double montoTotal = diasEstadia * habitacion.getPrecioNoche();
        log.info("Cálculo: {} días a ${} por noche = ${}", diasEstadia, habitacion.getPrecioNoche(), montoTotal);

        Reserva reserva = new Reserva();
        reserva.setHabitacionId(dto.getHabitacionId());
        reserva.setHuespedId(dto.getHuespedId());
        reserva.setFechaEntrada(dto.getFechaEntrada());
        reserva.setFechaSalida(dto.getFechaSalida());
        reserva.setMontoTotal(montoTotal);
        reserva.setEstado("CONFIRMADA");

        Reserva guardada = repository.save(reserva);
        log.info("Reserva ID {} creada exitosamente", guardada.getId());

        return mapper.toDTO(guardada);
    }

    public List<ReservaDTO> listarTodas(){
        log.info("Consultando el historia de reservas");
        return repository.findAll().stream()
            .map(mapper::toDTO)
            .collect(Collectors.toList());
    }
}
