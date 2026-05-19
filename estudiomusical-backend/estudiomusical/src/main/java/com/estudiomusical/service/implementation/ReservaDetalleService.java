package com.estudiomusical.service.implementation;

import com.estudiomusical.model.ReservaDetalle;
import com.estudiomusical.repository.IReservaDetalleRepository;
import com.estudiomusical.service.IReservaDetalleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        // Validamos existencia para que PUT no cree registros con IDs inexistentes.
        if (!repo.existsById(id)) {
            throw notFound(id);
        }
        reservaDetalle.setIdReservaDetalle(id);
        return repo.save(reservaDetalle);
    }

    @Override
    public List<ReservaDetalle> findAll() throws Exception {
        return repo.findAll();
    }

    @Override
    public ReservaDetalle findById(Integer id) throws Exception {
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
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Detalle de reserva no encontrado: " + id);
    }
}
