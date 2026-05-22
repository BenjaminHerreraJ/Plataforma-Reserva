package com.hotel.booking.mapper;

import org.springframework.stereotype.Component;

import com.hotel.booking.dto.ReservaDTO;
import com.hotel.booking.model.Reserva;

@Component
public class ReservaMapper {
    public ReservaDTO toDTO(Reserva entidad) {
        if (entidad == null) return null;
        ReservaDTO dto = new ReservaDTO();
        dto.setId(entidad.getId());
        dto.setHabitacionId(entidad.getHabitacionId());
        dto.setHuespedId(entidad.getHuespedId());
        dto.setFechaEntrada(entidad.getFechaEntrada());
        dto.setFechaSalida(entidad.getFechaSalida());
        dto.setMontoTotal(entidad.getMontoTotal());
        dto.setEstado(entidad.getEstado());
        return dto;
    }
}
