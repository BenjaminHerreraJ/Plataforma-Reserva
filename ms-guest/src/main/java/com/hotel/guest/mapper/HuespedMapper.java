package com.hotel.guest.mapper;

import org.springframework.stereotype.Component;

import com.hotel.guest.dto.HuespedDTO;
import com.hotel.guest.model.Huesped;

@Component
public class HuespedMapper {

    public HuespedDTO toDTO(Huesped entidad) {
        if (entidad == null) return null;
        HuespedDTO dto = new HuespedDTO();
        dto.setId(entidad.getId());
        dto.setRutPasaporte(entidad.getRutPasaporte());
        dto.setNombre(entidad.getNombre());
        dto.setEmail(entidad.getEmail());
        dto.setTelefono(entidad.getTelefono());
        return dto;
    }

    public Huesped toEntity(HuespedDTO dto) {
        if (dto == null) return null;
        Huesped entidad = new Huesped();
        entidad.setId(dto.getId());
        entidad.setRutPasaporte(dto.getRutPasaporte());
        entidad.setNombre(dto.getNombre());
        entidad.setEmail(dto.getEmail());
        entidad.setTelefono(dto.getTelefono());
        return entidad;
    }

}
