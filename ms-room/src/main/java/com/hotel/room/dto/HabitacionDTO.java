package com.hotel.room.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HabitacionDTO {

    private Long id;
    
    @NotBlank(message = "El numero de habitacion es obligatorio")
    private String numero;

    @NotBlank(message = "El tipo de habitacion es obligatorio")
    private String tipo;

    @NotNull(message = "El precio es obligatorio")
    @Min(value = 1, message = "El precio debe ser mayor a 0")
    private Double precioNoche;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;
    

}
