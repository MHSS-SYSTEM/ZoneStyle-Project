package com.estudio.zonestyleapi.service;

import com.estudio.zonestyleapi.model.Reserva;
import com.estudio.zonestyleapi.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReservaService {
    @Autowired
    private ReservaRepository repository;

    public List<Reserva> listarTodos() {
        return repository.findAll();
    }

    public Reserva guardar(Reserva r) {
        return repository.save(r);
    }
}