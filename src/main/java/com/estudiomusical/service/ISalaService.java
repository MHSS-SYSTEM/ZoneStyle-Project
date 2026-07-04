package com.estudiomusical.service;

import com.estudiomusical.model.Sala;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ISalaService extends IGenericService<Sala, Integer> {

    Page<Sala> listPage(Pageable pageable);
}