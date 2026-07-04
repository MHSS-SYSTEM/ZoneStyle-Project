package com.estudiomusical.service.implementation;

import com.estudiomusical.model.Sala;
import com.estudiomusical.repository.IGenericRepository;
import com.estudiomusical.repository.ISalaRepository;
import com.estudiomusical.service.ISalaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SalaService extends GenericService<Sala, Integer> implements ISalaService {
    // Autowired
    private final ISalaRepository repo;

    @Override
    protected IGenericRepository<Sala, Integer> getRepo() {
        return repo;
    }

    @Override
    public Page<Sala> listPage(Pageable pageable) {
        return repo.findAll(pageable);
    }

    /*
    @Override
    public Sala save(Sala sala) throws Exception {
        return repo.save(sala);
    }

    @Override
    public Sala update(Sala sala, Integer id) throws Exception {
        // EVALUAR CON EL ID
        // API REFLECTION
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

     */
}