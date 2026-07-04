package com.estudiomusical.service;

import com.estudiomusical.model.Servicio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IServicioService extends IGenericService<Servicio, Integer> {

    Page<Servicio> listPage(Pageable pageable);
}