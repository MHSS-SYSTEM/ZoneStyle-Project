package com.estudiomusical.service.implementation;

import com.estudiomusical.model.Pago;
import com.estudiomusical.model.Reserva;
import com.estudiomusical.model.ReservaDetalle;
import com.estudiomusical.repository.IGenericRepository;
import com.estudiomusical.repository.IReservaRepository;
import com.estudiomusical.repository.IPagoRepository;
import com.estudiomusical.service.IReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaService extends GenericService<Reserva, Integer> implements IReservaService {

    private final IReservaRepository repo;
    private final IPagoRepository pagoRepo;

    @Override
    protected IGenericRepository<Reserva, Integer> getRepo() {
        return repo;
    }

    @Override
    public Page<Reserva> listPage(Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Reserva save(Reserva reserva) throws Exception {
        prepararHorario(reserva);
        prepararEstado(reserva);
        validarCruceHorario(reserva, null);
        validarCruceEquipos(reserva, null);
        Double total = reserva.getTotal() != null ? reserva.getTotal() : 0.0;
        reserva.setTotal(total);
        Double abono = reserva.getAbono();
        if (abono == null) {
            abono = 0.0;
            reserva.setAbono(0.0);
        }

        // Validar abono inicial (mínimo 50%)
        if (total != null && abono < (total * 0.5)) {
            throw new IllegalArgumentException("El abono inicial debe ser como mínimo el 50% del total de la reserva (Total: S/." + total + ", Mínimo requerido: S/." + (total * 0.5) + ")");
        }

        // Calcular saldo automáticamente
        reserva.setSaldo(total - abono);

        if (reserva.getDetalles() != null) {
            for (ReservaDetalle detalle : reserva.getDetalles()) {
                detalle.setReserva(reserva);
            }
        }

        Reserva savedReserva = repo.save(reserva);

        // Crear automáticamente el registro de Pago (Abono Inicial)
        Pago pago = new Pago();
        pago.setReserva(savedReserva);
        pago.setFechaPago(LocalDateTime.now());
        pago.setMonto(abono);
        pago.setMetodoPago(reserva.getMetodoPago() != null ? reserva.getMetodoPago() : "EFECTIVO");
        pago.setTipoPago("ABONO_INICIAL");
        pagoRepo.save(pago);

        return savedReserva;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Reserva update(Reserva reserva, Integer id) throws Exception {
        reserva.setIdReserva(id);
        prepararHorario(reserva);
        prepararEstado(reserva);
        validarCruceHorario(reserva, id);
        validarCruceEquipos(reserva, id);
        Double total = reserva.getTotal() != null ? reserva.getTotal() : 0.0;
        reserva.setTotal(total);
        Double abono = reserva.getAbono();
        if (abono == null) {
            abono = 0.0;
            reserva.setAbono(0.0);
        }

        // Validar abono inicial (mínimo 50%)
        if (total != null && abono < (total * 0.5)) {
            throw new IllegalArgumentException("El abono inicial debe ser como mínimo el 50% del total de la reserva (Total: S/." + total + ", Mínimo requerido: S/." + (total * 0.5) + ")");
        }

        // Calcular saldo automáticamente
        reserva.setSaldo(total - abono);

        if (reserva.getDetalles() != null) {
            for (ReservaDetalle detalle : reserva.getDetalles()) {
                detalle.setReserva(reserva);
            }
        }

        Reserva savedReserva = repo.save(reserva);

        // Opcional: registrar liquidación si el abono cubre el 100% y antes no estaba pagado, o actualizar registro
        // Para simplificar, registramos un pago si el abono se actualiza
        return savedReserva;
    }

    @Override
    public List<Reserva> findByFecha(LocalDate inicio, LocalDate fin) throws Exception {
        return repo.findByFechaBetween(inicio.atStartOfDay(), fin.plusDays(1).atStartOfDay().minusNanos(1));
    }

    @Override
    public List<Reserva> findByCliente(Integer idCliente) throws Exception {
        return repo.findByCliente_IdCliente(idCliente);
    }

    @Override
    public List<Reserva> findBySala(Integer idSala) throws Exception {
        return repo.findBySala_IdSala(idSala);
    }

    @Override
    public List<Reserva> findByEstado(String estado) throws Exception {
        return repo.findByEstado(estado);
    }

    @Override
    public List<Reserva> findPendientesPago() throws Exception {
        return repo.findAll().stream()
                .filter(reserva -> reserva.getSaldo() != null && reserva.getSaldo() > 0)
                .filter(reserva -> reserva.getEstado() == null || !"CANCELADA".equalsIgnoreCase(reserva.getEstado()))
                .toList();
    }

    @Override
    public boolean existeDisponibilidad(Integer salaId, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, Integer excluirReservaId) throws Exception {
        Reserva reserva = new Reserva();
        reserva.setSala(new com.estudiomusical.model.Sala());
        reserva.getSala().setIdSala(salaId);
        reserva.setFecha(LocalDateTime.of(fecha, horaInicio));
        reserva.setHoraInicio(horaInicio);
        reserva.setHoraFin(horaFin);
        return !existeCruceHorario(reserva, excluirReservaId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Reserva cambiarEstado(Integer id, String estado) throws Exception {
        Reserva reserva = findById(id);
        reserva.setEstado(estado);
        return repo.save(reserva);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Reserva cancelar(Integer id) throws Exception {
        return cambiarEstado(id, "CANCELADA");
    }

    private void prepararHorario(Reserva reserva) {
        if (reserva.getFecha() == null) {
            throw new IllegalArgumentException("La fecha de la reserva es obligatoria");
        }

        if (reserva.getHoraInicio() == null) {
            reserva.setHoraInicio(reserva.getFecha().toLocalTime());
        }

        if (reserva.getHoraFin() == null) {
            reserva.setHoraFin(reserva.getHoraInicio().plusHours(1));
        }

        if (!reserva.getHoraFin().isAfter(reserva.getHoraInicio())) {
            throw new IllegalArgumentException("La hora fin debe ser mayor que la hora inicio");
        }

        reserva.setFecha(LocalDateTime.of(reserva.getFecha().toLocalDate(), reserva.getHoraInicio()));
    }

    private void prepararEstado(Reserva reserva) {
        if (reserva.getEstado() == null || reserva.getEstado().isBlank()) {
            reserva.setEstado("CONFIRMADA");
        }
    }

    private void validarCruceHorario(Reserva reserva, Integer idActual) {
        if ("CANCELADA".equalsIgnoreCase(reserva.getEstado())) {
            return;
        }

        if (existeCruceHorario(reserva, idActual)) {
            throw new IllegalArgumentException("La sala seleccionada ya tiene una reserva en ese bloque horario");
        }
    }

    private boolean existeCruceHorario(Reserva reserva, Integer idActual) {
        if (reserva.getSala() == null || reserva.getSala().getIdSala() == null) {
            throw new IllegalArgumentException("La sala es obligatoria para validar disponibilidad");
        }

        LocalDateTime inicioDia = reserva.getFecha().toLocalDate().atStartOfDay();
        LocalDateTime finDia = inicioDia.plusDays(1).minusNanos(1);
        LocalTime inicio = reserva.getHoraInicio();
        LocalTime fin = reserva.getHoraFin();

        List<Reserva> reservasDia = repo.findByFechaBetween(inicioDia, finDia);
        return reservasDia.stream()
                .filter(item -> idActual == null || !item.getIdReserva().equals(idActual))
                .filter(item -> item.getEstado() == null || !"CANCELADA".equalsIgnoreCase(item.getEstado()))
                .filter(item -> item.getSala() != null && item.getSala().getIdSala().equals(reserva.getSala().getIdSala()))
                .anyMatch(item -> {
                    LocalTime itemInicio = item.getHoraInicio() != null ? item.getHoraInicio() : item.getFecha().toLocalTime();
                    LocalTime itemFin = item.getHoraFin() != null ? item.getHoraFin() : itemInicio.plusHours(1);
                    return inicio.isBefore(itemFin) && fin.isAfter(itemInicio);
                });
    }

    private void validarCruceEquipos(Reserva reserva, Integer idActual) {
        if (reserva.getEquipos() == null || reserva.getEquipos().isEmpty() || "CANCELADA".equalsIgnoreCase(reserva.getEstado())) {
            return;
        }

        List<Integer> equiposSolicitados = reserva.getEquipos().stream()
                .map(com.estudiomusical.model.Equipo::getIdEquipo)
                .toList();

        LocalDateTime inicioDia = reserva.getFecha().toLocalDate().atStartOfDay();
        LocalDateTime finDia = inicioDia.plusDays(1).minusNanos(1);
        LocalTime inicio = reserva.getHoraInicio();
        LocalTime fin = reserva.getHoraFin();

        boolean existeCruce = repo.findByFechaBetween(inicioDia, finDia).stream()
                .filter(item -> idActual == null || !item.getIdReserva().equals(idActual))
                .filter(item -> item.getEstado() == null || !"CANCELADA".equalsIgnoreCase(item.getEstado()))
                .filter(item -> item.getEquipos() != null && !item.getEquipos().isEmpty())
                .filter(item -> {
                    LocalTime itemInicio = item.getHoraInicio() != null ? item.getHoraInicio() : item.getFecha().toLocalTime();
                    LocalTime itemFin = item.getHoraFin() != null ? item.getHoraFin() : itemInicio.plusHours(1);
                    return inicio.isBefore(itemFin) && fin.isAfter(itemInicio);
                })
                .flatMap(item -> item.getEquipos().stream())
                .anyMatch(equipo -> equiposSolicitados.contains(equipo.getIdEquipo()));

        if (existeCruce) {
            throw new IllegalArgumentException("Uno o mas equipos seleccionados ya estan asignados en ese bloque horario");
        }
    }
}