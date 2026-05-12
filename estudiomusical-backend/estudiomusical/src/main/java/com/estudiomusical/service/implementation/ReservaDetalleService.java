package com.estudiomusical.service.implementation;

import com.estudiomusical.model.ReservaDetalle;
import com.estudiomusical.repository.IReservaDetalleRepository;
import com.estudiomusical.service.IReservaDetalleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaDetalleService implements IReservaDetalleService {

    private final IReservaDetalleRepository repo;

    @Override
    public ReservaDetalle save(ReservaDetalle reservaDetalle) throws Exception {
        return repo.save(reservaDetalle);
    }

    @Override
    public ReservaDetalle update(ReservaDetalle reservaDetalle, Integer id) throws Exception {
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
}
