package com.estudiomusical.service.implementation;

import com.estudiomusical.model.Equipo;
import com.estudiomusical.model.Reserva;
import com.estudiomusical.repository.IGenericRepository;
import com.estudiomusical.repository.IEquipoRepository;
import com.estudiomusical.repository.IReservaRepository;
import com.estudiomusical.service.IEquipoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EquipoService extends GenericService<Equipo, Integer> implements IEquipoService {

    private final IEquipoRepository repo;
    private final IReservaRepository reservaRepo;

    @Override
    protected IGenericRepository<Equipo, Integer> getRepo() {
        return repo;
    }

    @Override
    public Page<Equipo> listPage(Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Override
    public List<Equipo> findDisponibles(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) throws Exception {
        LocalDateTime inicioDia = fecha.atStartOfDay();
        LocalDateTime finDia = inicioDia.plusDays(1).minusNanos(1);

        Set<Integer> equiposOcupados = reservaRepo.findByFechaBetween(inicioDia, finDia).stream()
                .filter(reserva -> reserva.getEstado() == null || !"CANCELADA".equalsIgnoreCase(reserva.getEstado()))
                .filter(reserva -> cruzaHorario(reserva, horaInicio, horaFin))
                .filter(reserva -> reserva.getEquipos() != null)
                .flatMap(reserva -> reserva.getEquipos().stream())
                .map(Equipo::getIdEquipo)
                .collect(Collectors.toSet());

        return repo.findAll().stream()
                .filter(equipo -> "DISPONIBLE".equalsIgnoreCase(equipo.getEstado()))
                .filter(equipo -> !equiposOcupados.contains(equipo.getIdEquipo()))
                .toList();
    }

    private boolean cruzaHorario(Reserva reserva, LocalTime horaInicio, LocalTime horaFin) {
        LocalTime inicioReserva = reserva.getHoraInicio() != null ? reserva.getHoraInicio() : reserva.getFecha().toLocalTime();
        LocalTime finReserva = reserva.getHoraFin() != null ? reserva.getHoraFin() : inicioReserva.plusHours(1);
        return horaInicio.isBefore(finReserva) && horaFin.isAfter(inicioReserva);
    }
}