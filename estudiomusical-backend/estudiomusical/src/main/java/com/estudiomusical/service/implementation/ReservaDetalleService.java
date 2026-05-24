package com.estudiomusical.service.implementation;

import com.estudiomusical.model.ReservaDetalle;
import com.estudiomusical.repository.IGenericRepository;
import com.estudiomusical.repository.IReservaDetalleRepository;
import com.estudiomusical.service.IReservaDetalleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservaDetalleService extends GenericService<ReservaDetalle, Integer> implements IReservaDetalleService {
    // Autowired
    private final IReservaDetalleRepository repo;

    @Override
    protected IGenericRepository<ReservaDetalle, Integer> getRepo() {
        return repo;
    }

    /*
    @Override
    public ReservaDetalle save(ReservaDetalle reservaDetalle) throws Exception {
        return repo.save(reservaDetalle);
    }

    @Override
    public ReservaDetalle update(ReservaDetalle reservaDetalle, Integer id) throws Exception {
        // EVALUAR CON EL ID
        // API REFLECTION
        reservaDetalle.setIdReservaDetalle(id);
        return repo.save(reservaDetalle);
    }

    @Override
    public List<ReservaDetalle> findAll() throws Exception {
        return repo.findAll();
    }

    @Override
    public ReservaDetalle findById(Integer id) throws Exception {
        return repo.findById(id).orElse(new ReservaDetalle());
    }

    @Override
    public void delete(Integer id) throws Exception {
        repo.deleteById(id);
    }

     */
}
