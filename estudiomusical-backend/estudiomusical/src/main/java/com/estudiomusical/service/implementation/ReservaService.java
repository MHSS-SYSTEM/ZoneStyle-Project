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

        // VALIDAR SI LA SALA YA ESTÁ OCUPADA
        boolean ocupada = repo.salaOcupada(
                reserva.getSala().getIdSala(),
                reserva.getFecha()
        );

        if (ocupada) {
            throw new Exception("La sala ya está reservada");
        }

        double total = 0;

        // RECORRER DETALLES
        if (reserva.getDetalles() != null) {

            for (ReservaDetalle detalle : reserva.getDetalles()) {

                // RELACIONAR DETALLE CON RESERVA
                detalle.setReserva(reserva);

                // CALCULAR SUBTOTAL
                double subtotal =
                        detalle.getCantidadHoras() *
                                detalle.getServicio().getPrecioPorHora();

                detalle.setSubtotal(subtotal);

                // ACUMULAR TOTAL
                total += subtotal;
            }
        }

        // ASIGNAR TOTAL AUTOMÁTICO
        reserva.setTotal(total);

        return repo.save(reserva);
    }

    @Override
    public Reserva update(Reserva reserva, Integer id) throws Exception {

        Reserva reservaExistente = repo.findById(id)
                .orElseThrow(() ->
                        new Exception("Reserva no encontrada"));

        reserva.setIdReserva(id);

        double total = 0;

        if (reserva.getDetalles() != null) {

            for (ReservaDetalle detalle : reserva.getDetalles()) {

                detalle.setReserva(reserva);

                double subtotal =
                        detalle.getCantidadHoras() *
                                detalle.getServicio().getPrecioPorHora();

                detalle.setSubtotal(subtotal);

                total += subtotal;
            }
        }

        reserva.setTotal(total);

        return repo.save(reserva);
    }

    @Override
    public List<Reserva> findAll() throws Exception {
        return repo.findAll();
    }

    @Override
    public Reserva findById(Integer id) throws Exception {

        return repo.findById(id)
                .orElseThrow(() ->
                        new Exception("Reserva no encontrada"));
    }

    @Override
    public void delete(Integer id) throws Exception {

        Reserva reserva = repo.findById(id)
                .orElseThrow(() ->
                        new Exception("Reserva no encontrada"));

        repo.delete(reserva);
    }
}