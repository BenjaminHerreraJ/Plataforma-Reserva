package com.hotel.booking.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "room-service", url = "http://localhost:8080/api/v1/habitaciones")
public interface HabitacionCliente {
    @GetMapping("/{id}")
    HabitacionDTO obtenerHabitacionPorId(@PathVariable("id") Long id);
}
