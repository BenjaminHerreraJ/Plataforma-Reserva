package com.hotel.guest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class HuespedDTO {

    private Long id;

    @NotBlank(message = "El RUT o Pasaporte es obligatorio")
    private String rutPasaporte;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Email(message = "El formato del email no es válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    private String telefono;
    

}
