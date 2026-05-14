package com.hotel.room.mapper;

import org.springframework.stereotype.Component;

import com.hotel.room.dto.HabitacionDTO;
import com.hotel.room.model.Habitacion;

@Component
public class HabitacionMapper {


    // Convierte de entidad a dto (esto se manda al cliente)
    public HabitacionDTO toDTO(Habitacion entidad){
        if(entidad == null){
            return null;
        }
        HabitacionDTO dto = new HabitacionDTO();
        dto.setId(entidad.getId());
        dto.setNumero(entidad.getNumero());
        dto.setTipo(entidad.getTipo());
        dto.setPrecioNoche(entidad.getPrecioNoche());
        dto.setEstado(entidad.getEstado());
        return dto;
    }

    // COnvierte de dto a entidad(este se guarda en la BD)
    // NOTA: Posible cambio en entidad ID
    public Habitacion toEntity(HabitacionDTO dto){
        if(dto == null){
            return null;
        }
        Habitacion entidad = new Habitacion();
        entidad.setId(dto.getId());
        entidad.setNumero(dto.getNumero());
        entidad.setTipo(dto.getTipo());
        entidad.setPrecioNoche(dto.getPrecioNoche());
        entidad.setEstado(dto.getEstado());
        return entidad;
    }

}
