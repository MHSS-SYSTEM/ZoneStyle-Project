package com.estudiomusical.repository;

import com.estudiomusical.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface IReservaRepository extends JpaRepository<Reserva, Integer> {

    @Query("""
        SELECT COUNT(r) > 0
        FROM Reserva r
        WHERE r.sala.idSala = :idSala
        AND r.fecha = :fecha
    """)
    boolean salaOcupada(
            @Param("idSala") Integer idSala,
            @Param("fecha") LocalDateTime fecha
    );
}