package com.hotel.guest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.guest.model.Huesped;


public interface  HuespedRepository extends JpaRepository<Huesped, Long> {
    Optional<Huesped> findByRutPasaporte(String rutPasaporte);
}
