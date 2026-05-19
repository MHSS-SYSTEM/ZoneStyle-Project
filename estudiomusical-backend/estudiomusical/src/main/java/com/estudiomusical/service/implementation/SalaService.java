package com.estudiomusical.service.implementation;

import com.estudiomusical.model.Sala;
import com.estudiomusical.repository.ISalaRepository;
import com.estudiomusical.service.ISalaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        // Validamos existencia para que PUT no cree registros con IDs inexistentes.
        if (!repo.existsById(id)) {
            throw notFound(id);
        }
        sala.setIdSala(id);
        return repo.save(sala);
    }

    @Override
    public List<Sala> findAll() throws Exception {
        return repo.findAll();
    }

    @Override
    public Sala findById(Integer id) throws Exception {
        return repo.findById(id).orElseThrow(() -> notFound(id));
    }

    @Override
    public void delete(Integer id) throws Exception {
        if (!repo.existsById(id)) {
            throw notFound(id);
        }
        repo.deleteById(id);
    }

    private ResponseStatusException notFound(Integer id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Sala no encontrada: " + id);
    }
}
