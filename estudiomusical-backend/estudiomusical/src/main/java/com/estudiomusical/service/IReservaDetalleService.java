package com.estudiomusical.service;

import com.estudiomusical.model.ReservaDetalle;

import java.util.List;

public interface IReservaDetalleService {

    ReservaDetalle save(ReservaDetalle reservaDetalle) throws Exception;

    ReservaDetalle update(ReservaDetalle reservaDetalle, Integer id) throws Exception;

    List<ReservaDetalle> findAll() throws Exception;

    ReservaDetalle findById(Integer id) throws Exception;

    void delete(Integer id) throws Exception;
}
