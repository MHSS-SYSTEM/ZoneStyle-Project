package com.estudiomusical.repository;

import com.estudiomusical.model.Pago;

import java.time.LocalDateTime;
import java.util.List;

public interface IPagoRepository extends IGenericRepository<Pago, Integer> {
    List<Pago> findByReserva_IdReservaOrderByFechaPagoDesc(Integer idReserva);

    List<Pago> findByFechaPagoBetween(LocalDateTime inicio, LocalDateTime fin);
}
