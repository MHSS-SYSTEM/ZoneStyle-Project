package com.estudiomusical.service.implementation;

import com.estudiomusical.model.Servicio;
import com.estudiomusical.repository.IServicioRepository;
import com.estudiomusical.service.IServicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        servicio.setIdServicio(id);
        return repo.save(servicio);
    }

    @Override
    public List<Servicio> findAll() throws Exception {
        return repo.findAll();
    }

    @Override
    public Servicio findById(Integer id) throws Exception {
        return repo.findById(id).orElse(new Servicio());
    }

    @Override
    public void delete(Integer id) throws Exception {
        repo.deleteById(id);
    }
}
