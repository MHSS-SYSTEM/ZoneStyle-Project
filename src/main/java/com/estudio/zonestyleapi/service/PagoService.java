package com.estudio.zonestyleapi.service;

import com.estudio.zonestyleapi.model.Pago; // Asegúrate que sea Pago
import com.estudio.zonestyleapi.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PagoService {
    @Autowired
    private PagoRepository repository;

    public List<Pago> listarTodos() {
        return repository.findAll();
    }

    public Pago guardar(Pago p) {
        return repository.save(p);
    }
}