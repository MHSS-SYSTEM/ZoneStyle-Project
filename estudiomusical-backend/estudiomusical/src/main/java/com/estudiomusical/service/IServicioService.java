package com.estudiomusical.service;

import com.estudiomusical.model.Servicio;

import java.util.List;

public interface IServicioService {

    Servicio save(Servicio servicio) throws Exception;

    Servicio update(Servicio servicio, Integer id) throws Exception;

    List<Servicio> findAll() throws Exception;

    Servicio findById(Integer id) throws Exception;

    void delete(Integer id) throws Exception;
}
