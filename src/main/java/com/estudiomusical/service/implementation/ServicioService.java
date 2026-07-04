package com.estudiomusical.service.implementation;

import com.estudiomusical.model.Servicio;
import com.estudiomusical.repository.IGenericRepository;
import com.estudiomusical.repository.IServicioRepository;
import com.estudiomusical.service.IServicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServicioService extends GenericService<Servicio, Integer> implements IServicioService {
    // Autowired
    private final IServicioRepository repo;

    @Override
    protected IGenericRepository<Servicio, Integer> getRepo() {
        return repo;
    }

    @Override
    public Page<Servicio> listPage(Pageable pageable) {
        return repo.findAll(pageable);
    }

    /*
    @Override
    public Servicio save(Servicio servicio) throws Exception {
        return repo.save(servicio);
    }

    @Override
    public Servicio update(Servicio servicio, Integer id) throws Exception {
        // EVALUAR CON EL ID
        // API REFLECTION
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

     */
}