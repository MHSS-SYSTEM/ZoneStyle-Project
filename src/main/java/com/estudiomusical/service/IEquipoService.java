package com.estudiomusical.service;

import com.estudiomusical.model.Equipo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface IEquipoService extends IGenericService<Equipo, Integer> {
    List<Equipo> findDisponibles(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) throws Exception;
}
