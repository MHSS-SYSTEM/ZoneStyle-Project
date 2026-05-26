package com.estudiomusical.service.implementation;

import com.estudiomusical.model.Reserva;
import com.estudiomusical.model.ReservaDetalle;
import com.estudiomusical.repository.IGenericRepository;
import com.estudiomusical.repository.IReservaRepository;
import com.estudiomusical.service.IReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservaService extends GenericService<Reserva, Integer> implements IReservaService {
    // Autowired
    private final IReservaRepository repo;

    @Override
    protected IGenericRepository<Reserva, Integer> getRepo() {
        return repo;
    }

    @Override
    public Reserva save(Reserva reserva) throws Exception {
        // EVALUAR CON EL ID
        // API REFLECTION
        if (reserva.getDetalles() != null) {
            for (ReservaDetalle detalle : reserva.getDetalles()) {
                detalle.setReserva(reserva);
            }
        }
        return repo.save(reserva);
    }

    @Override
    public Reserva update(Reserva reserva, Integer id) throws Exception {
        // EVALUAR CON EL ID
        // API REFLECTION
        reserva.setIdReserva(id);
        if (reserva.getDetalles() != null) {
            for (ReservaDetalle detalle : reserva.getDetalles()) {
                detalle.setReserva(reserva);
            }
        }
        return repo.save(reserva);
    }
}
