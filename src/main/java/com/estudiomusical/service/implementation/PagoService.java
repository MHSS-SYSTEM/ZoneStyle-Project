package com.estudiomusical.service.implementation;

import com.estudiomusical.model.Pago;
import com.estudiomusical.model.Reserva;
import com.estudiomusical.repository.IGenericRepository;
import com.estudiomusical.repository.IPagoRepository;
import com.estudiomusical.repository.IReservaRepository;
import com.estudiomusical.service.IPagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoService extends GenericService<Pago, Integer> implements IPagoService {

    private final IPagoRepository repo;
    private final IReservaRepository reservaRepo;

    @Override
    protected IGenericRepository<Pago, Integer> getRepo() {
        return repo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Pago registrarPagoReserva(Integer idReserva, Pago pago) throws Exception {
        Reserva reserva = reservaRepo.findById(idReserva)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada: " + idReserva));

        double monto = pago.getMonto() != null ? pago.getMonto() : 0.0;
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto del pago debe ser mayor a cero");
        }

        double saldoActual = reserva.getSaldo() != null ? reserva.getSaldo() : 0.0;
        if (monto > saldoActual) {
            throw new IllegalArgumentException("El pago no puede superar el saldo pendiente de S/." + saldoActual);
        }

        reserva.setAbono((reserva.getAbono() != null ? reserva.getAbono() : 0.0) + monto);
        reserva.setSaldo(saldoActual - monto);
        reservaRepo.save(reserva);

        pago.setReserva(reserva);
        pago.setFechaPago(pago.getFechaPago() != null ? pago.getFechaPago() : LocalDateTime.now());
        pago.setMetodoPago(pago.getMetodoPago() != null ? pago.getMetodoPago() : "EFECTIVO");
        pago.setTipoPago(pago.getTipoPago() != null ? pago.getTipoPago() : "PAGO_PARCIAL");

        return repo.save(pago);
    }

    @Override
    public List<Pago> findByReserva(Integer idReserva) throws Exception {
        return repo.findByReserva_IdReservaOrderByFechaPagoDesc(idReserva);
    }
}
