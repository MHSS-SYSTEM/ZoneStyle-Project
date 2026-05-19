package com.estudiomusical.service.implementation;

import com.estudiomusical.model.Servicio;
import com.estudiomusical.repository.IServicioRepository;
import com.estudiomusical.service.IServicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicioService implements IServicioService {

    private final IServicioRepository repo;

    @Override
    public Servicio save(Servicio servicio) throws Exception {
        return repo.save(servicio);
    }

    @Override
    public Servicio update(Servicio servicio, Integer id) throws Exception {
        // Validamos existencia para que PUT no cree registros con IDs inexistentes.
        if (!repo.existsById(id)) {
            throw notFound(id);
        }
        servicio.setIdServicio(id);
        return repo.save(servicio);
    }

    @Override
    public List<Servicio> findAll() throws Exception {
        return repo.findAll();
    }

    @Override
    public Servicio findById(Integer id) throws Exception {
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
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Servicio no encontrado: " + id);
    }
}
