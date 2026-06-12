package com.estudiomusical.service;

import com.estudiomusical.model.Pago;

import java.util.List;

public interface IPagoService extends IGenericService<Pago, Integer> {
    Pago registrarPagoReserva(Integer idReserva, Pago pago) throws Exception;

    List<Pago> findByReserva(Integer idReserva) throws Exception;
}
