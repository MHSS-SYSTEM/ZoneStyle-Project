package com.upn.avionapi.service;

import com.upn.avionapi.model.Avion;
import com.upn.avionapi.repository.AvionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AvionServiceImpl implements IAvionService {

    @Autowired
    private AvionRepository repository;

    @Override
    public List<Avion> listarTodos() { return repository.findAll(); }

    @Override
    public Avion guardar(Avion avion) { return repository.save(avion); }

    @Override
    public Avion buscarPorId(Long id) { return repository.findById(id).orElse(null); }

    @Override
    public void eliminar(Long id) { repository.deleteById(id); }
}