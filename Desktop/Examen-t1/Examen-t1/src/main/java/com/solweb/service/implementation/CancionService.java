package com.solweb.service.implementation;

import com.solweb.model.Cancion;
import com.solweb.repository.ICancionRepository;
import com.solweb.service.ICancionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CancionService implements ICancionService {

    private final ICancionRepository repo;

    @Override
    public List<Cancion> findAll() {
        return repo.findAll();
    }

    @Override
    public Optional<Cancion> findById(Integer id) {
        return repo.findById(id);
    }

    @Override
    public Cancion save(Cancion cancion) {
        return repo.save(cancion);
    }

    @Override
    public Cancion update(Integer id, Cancion cancion) {
        cancion.setId(id);
        return repo.save(cancion);
    }

    @Override
    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
