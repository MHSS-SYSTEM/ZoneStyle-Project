package com.estudiomusical.repository;

import com.estudiomusical.model.Reserva;

import java.time.LocalDateTime;
import java.util.List;

public interface IReservaRepository extends IGenericRepository<Reserva, Integer> {
    List<Reserva> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);

    List<Reserva> findByCliente_IdCliente(Integer idCliente);

    List<Reserva> findBySala_IdSala(Integer idSala);

    List<Reserva> findByEstado(String estado);
}
