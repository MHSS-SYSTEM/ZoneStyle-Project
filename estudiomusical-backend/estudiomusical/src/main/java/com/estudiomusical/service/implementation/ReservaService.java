package com.estudiomusical.service.implementation;

import com.estudiomusical.model.Reserva;
import com.estudiomusical.model.ReservaDetalle;
import com.estudiomusical.repository.IReservaRepository;
import com.estudiomusical.service.IReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        return repo.findById(id).orElse(new Reserva());
    }

    @Override
    public void delete(Integer id) throws Exception {
        repo.deleteById(id);
    }
}
