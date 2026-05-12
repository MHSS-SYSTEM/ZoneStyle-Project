package com.estudiomusical.service.implementation;

import com.estudiomusical.model.Sala;
import com.estudiomusical.repository.ISalaRepository;
import com.estudiomusical.service.ISalaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalaService implements ISalaService {

    private final ISalaRepository repo;

    @Override
    public Sala save(Sala sala) throws Exception {
        return repo.save(sala);
    }

    @Override
    public Sala update(Sala sala, Integer id) throws Exception {
        sala.setIdSala(id);
        return repo.save(sala);
    }

    @Override
    public List<Sala> findAll() throws Exception {
        return repo.findAll();
    }

    @Override
    public Sala findById(Integer id) throws Exception {
        return repo.findById(id).orElse(new Sala());
    }

    @Override
    public void delete(Integer id) throws Exception {
        repo.deleteById(id);
    }
}
