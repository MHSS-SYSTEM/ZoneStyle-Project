package com.upn.avionapi.service;

import com.upn.avionapi.model.Avion;
import java.util.List;

public interface IAvionService {
    List<Avion> listarTodos();
    Avion guardar(Avion avion);
    Avion buscarPorId(Long id);
    void eliminar(Long id);
}