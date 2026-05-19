package com.estudiomusical.service.implementation;

import com.estudiomusical.model.Reserva;
import com.estudiomusical.model.ReservaDetalle;
import com.estudiomusical.repository.IReservaRepository;
import com.estudiomusical.service.IReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaService implements IReservaService {

    private final IReservaRepository repo;

    @Override
    public Reserva save(Reserva reserva) throws Exception {
        // Enlazar cada detalle con su reserva padre antes de persistir
        if (reserva.getDetalles() != null) {
            for (ReservaDetalle detalle : reserva.getDetalles()) {
                detalle.setReserva(reserva);
            }
        }
        return repo.save(reserva);
    }

    @Override
    public Reserva update(Reserva reserva, Integer id) throws Exception {
        // Validamos existencia para que PUT no cree registros con IDs inexistentes.
        if (!repo.existsById(id)) {
            throw notFound(id);
        }
        reserva.setIdReserva(id);
        // Enlazar cada detalle con su reserva padre antes de persistir
        if (reserva.getDetalles() != null) {
            for (ReservaDetalle detalle : reserva.getDetalles()) {
                detalle.setReserva(reserva);
            }
        }
        return repo.save(reserva);
    }

    @Override
    public List<Reserva> findAll() throws Exception {
        return repo.findAll();
    }

    @Override
    public Reserva findById(Integer id) throws Exception {
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
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva no encontrada: " + id);
    }
}
