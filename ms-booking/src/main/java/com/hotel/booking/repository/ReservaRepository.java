package com.hotel.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.booking.model.Reserva;

public interface  ReservaRepository extends JpaRepository<Reserva, Long> {

}
