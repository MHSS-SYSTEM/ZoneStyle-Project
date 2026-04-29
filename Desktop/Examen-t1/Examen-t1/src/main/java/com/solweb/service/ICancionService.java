package com.solweb.service;

import com.solweb.model.Cancion;
import java.util.List;
import java.util.Optional;

public interface ICancionService {
    List<Cancion> findAll();
    Optional<Cancion> findById(Integer id);
    Cancion save(Cancion cancion);
    Cancion update(Integer id, Cancion cancion);
    void delete(Integer id);
}
