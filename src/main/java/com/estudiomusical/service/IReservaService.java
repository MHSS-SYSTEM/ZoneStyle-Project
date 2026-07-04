package com.estudiomusical.service;

import com.estudiomusical.model.Reserva;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface IReservaService extends IGenericService<Reserva, Integer> {
    List<Reserva> findByFecha(LocalDate inicio, LocalDate fin) throws Exception;

    List<Reserva> findByCliente(Integer idCliente) throws Exception;

    List<Reserva> findBySala(Integer idSala) throws Exception;

    List<Reserva> findByEstado(String estado) throws Exception;

    List<Reserva> findPendientesPago() throws Exception;

    boolean existeDisponibilidad(Integer salaId, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, Integer excluirReservaId) throws Exception;

    Reserva cambiarEstado(Integer id, String estado) throws Exception;

    Reserva cancelar(Integer id) throws Exception;

    Page<Reserva> listPage(Pageable pageable);
}