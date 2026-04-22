package com.estudio.zonestyleapi.repository;

import com.estudio.zonestyleapi.model.Reserva; //
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    // Aquí decía Cliente, cámbialo por Reserva
}