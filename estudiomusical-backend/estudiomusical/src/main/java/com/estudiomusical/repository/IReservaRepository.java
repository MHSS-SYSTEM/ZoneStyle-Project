package com.estudiomusical.repository;

import com.estudiomusical.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IReservaRepository extends JpaRepository<Reserva, Integer> {
}
