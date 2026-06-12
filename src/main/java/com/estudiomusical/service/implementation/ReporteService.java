package com.estudiomusical.service.implementation;

import com.estudiomusical.dto.reporte.ClienteFrecuenteDTO;
import com.estudiomusical.dto.reporte.EquipoUsadoDTO;
import com.estudiomusical.dto.reporte.IngresoPorFechaDTO;
import com.estudiomusical.dto.reporte.OcupacionSalaDTO;
import com.estudiomusical.dto.reporte.PagoPendienteDTO;
import com.estudiomusical.dto.reporte.ReporteResumenDTO;
import com.estudiomusical.dto.reporte.ReservasPorFechaDTO;
import com.estudiomusical.dto.reporte.ServicioSolicitadoDTO;
import com.estudiomusical.model.Pago;
import com.estudiomusical.model.Reserva;
import com.estudiomusical.model.ReservaDetalle;
import com.estudiomusical.repository.IClienteRepository;
import com.estudiomusical.repository.IPagoRepository;
import com.estudiomusical.repository.IReservaDetalleRepository;
import com.estudiomusical.repository.IReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final IReservaRepository reservaRepo;
    private final IPagoRepository pagoRepo;
    private final IClienteRepository clienteRepo;
    private final IReservaDetalleRepository reservaDetalleRepo;

    public ReporteResumenDTO resumen(LocalDate inicio, LocalDate fin) {
        List<Reserva> reservas = reservasEnRango(inicio, fin);
        List<Pago> pagos = pagosEnRango(inicio, fin);

        double ingresos = pagos.stream()
                .mapToDouble(pago -> pago.getMonto() != null ? pago.getMonto() : 0.0)
                .sum();

        double saldos = reservas.stream()
                .mapToDouble(reserva -> reserva.getSaldo() != null ? reserva.getSaldo() : 0.0)
                .sum();

        return new ReporteResumenDTO(
                (long) reservas.size(),
                clienteRepo.count(),
                ingresos,
                saldos
        );
    }

    public List<IngresoPorFechaDTO> ingresosPorFecha(LocalDate inicio, LocalDate fin) {
        Map<LocalDate, Double> agrupado = pagosEnRango(inicio, fin).stream()
                .collect(Collectors.groupingBy(
                        pago -> pago.getFechaPago().toLocalDate(),
                        TreeMap::new,
                        Collectors.summingDouble(pago -> pago.getMonto() != null ? pago.getMonto() : 0.0)
                ));

        return agrupado.entrySet().stream()
                .map(item -> new IngresoPorFechaDTO(item.getKey(), item.getValue()))
                .toList();
    }

    public List<ReservasPorFechaDTO> reservasPorFecha(LocalDate inicio, LocalDate fin) {
        Map<LocalDate, Long> agrupado = reservasEnRango(inicio, fin).stream()
                .collect(Collectors.groupingBy(
                        reserva -> reserva.getFecha().toLocalDate(),
                        TreeMap::new,
                        Collectors.counting()
                ));

        return agrupado.entrySet().stream()
                .map(item -> new ReservasPorFechaDTO(item.getKey(), item.getValue()))
                .toList();
    }

    public List<PagoPendienteDTO> pagosPendientes() throws Exception {
        return reservaRepo.findAll().stream()
                .filter(reserva -> reserva.getSaldo() != null && reserva.getSaldo() > 0)
                .sorted(Comparator.comparing(Reserva::getFecha))
                .map(reserva -> new PagoPendienteDTO(
                        reserva.getIdReserva(),
                        reserva.getCliente() != null ? reserva.getCliente().getNombre() : "",
                        reserva.getSala() != null ? reserva.getSala().getNombre() : "",
                        reserva.getFecha(),
                        reserva.getTotal(),
                        reserva.getAbono(),
                        reserva.getSaldo()
                ))
                .toList();
    }

    public List<ServicioSolicitadoDTO> serviciosMasSolicitados(LocalDate inicio, LocalDate fin) throws Exception {
        LocalDateTime inicioFecha = inicio.atStartOfDay();
        LocalDateTime finFecha = fin.plusDays(1).atStartOfDay().minusNanos(1);

        return reservaDetalleRepo.findAll().stream()
                .filter(detalle -> detalle.getReserva() != null && detalle.getReserva().getFecha() != null)
                .filter(detalle -> !detalle.getReserva().getFecha().isBefore(inicioFecha) && !detalle.getReserva().getFecha().isAfter(finFecha))
                .collect(Collectors.groupingBy(detalle -> detalle.getServicio().getIdServicio()))
                .values().stream()
                .map(this::crearServicioSolicitado)
                .sorted(Comparator.comparing(ServicioSolicitadoDTO::getCantidadReservas).reversed())
                .toList();
    }

    public List<OcupacionSalaDTO> ocupacionSalas(LocalDate inicio, LocalDate fin) {
        return reservasEnRango(inicio, fin).stream()
                .filter(reserva -> reserva.getSala() != null)
                .filter(reserva -> reserva.getEstado() == null || !"CANCELADA".equalsIgnoreCase(reserva.getEstado()))
                .collect(Collectors.groupingBy(reserva -> reserva.getSala().getIdSala()))
                .values().stream()
                .map(reservas -> {
                    Reserva primera = reservas.get(0);
                    int horas = reservas.stream().mapToInt(this::horasReserva).sum();
                    return new OcupacionSalaDTO(
                            primera.getSala().getIdSala(),
                            primera.getSala().getNombre(),
                            (long) reservas.size(),
                            horas
                    );
                })
                .sorted(Comparator.comparing(OcupacionSalaDTO::getReservas).reversed())
                .toList();
    }

    public List<ServicioSolicitadoDTO> ingresosPorServicio(LocalDate inicio, LocalDate fin) throws Exception {
        return serviciosMasSolicitados(inicio, fin).stream()
                .sorted(Comparator.comparing(ServicioSolicitadoDTO::getTotalGenerado).reversed())
                .toList();
    }

    public List<ClienteFrecuenteDTO> clientesFrecuentes(LocalDate inicio, LocalDate fin) {
        return reservasEnRango(inicio, fin).stream()
                .filter(reserva -> reserva.getCliente() != null)
                .filter(reserva -> reserva.getEstado() == null || !"CANCELADA".equalsIgnoreCase(reserva.getEstado()))
                .collect(Collectors.groupingBy(reserva -> reserva.getCliente().getIdCliente()))
                .values().stream()
                .map(reservas -> {
                    Reserva primera = reservas.get(0);
                    double total = reservas.stream()
                            .mapToDouble(reserva -> reserva.getTotal() != null ? reserva.getTotal() : 0.0)
                            .sum();
                    return new ClienteFrecuenteDTO(
                            primera.getCliente().getIdCliente(),
                            primera.getCliente().getNombre(),
                            (long) reservas.size(),
                            total
                    );
                })
                .sorted(Comparator.comparing(ClienteFrecuenteDTO::getReservas).reversed())
                .toList();
    }

    public List<EquipoUsadoDTO> equiposMasUsados(LocalDate inicio, LocalDate fin) {
        return reservasEnRango(inicio, fin).stream()
                .filter(reserva -> reserva.getEstado() == null || !"CANCELADA".equalsIgnoreCase(reserva.getEstado()))
                .filter(reserva -> reserva.getEquipos() != null)
                .flatMap(reserva -> reserva.getEquipos().stream())
                .collect(Collectors.groupingBy(equipo -> equipo.getIdEquipo()))
                .values().stream()
                .map(equipos -> new EquipoUsadoDTO(
                        equipos.get(0).getIdEquipo(),
                        equipos.get(0).getNombre(),
                        (long) equipos.size()
                ))
                .sorted(Comparator.comparing(EquipoUsadoDTO::getReservas).reversed())
                .toList();
    }

    private ServicioSolicitadoDTO crearServicioSolicitado(List<ReservaDetalle> detalles) {
        ReservaDetalle primero = detalles.get(0);
        int horas = detalles.stream()
                .mapToInt(detalle -> detalle.getCantidadHoras() != null ? detalle.getCantidadHoras() : 0)
                .sum();
        double total = detalles.stream()
                .mapToDouble(detalle -> detalle.getSubtotal() != null ? detalle.getSubtotal() : 0.0)
                .sum();

        return new ServicioSolicitadoDTO(
                primero.getServicio().getIdServicio(),
                primero.getServicio().getNombre(),
                (long) detalles.size(),
                horas,
                total
        );
    }

    private List<Reserva> reservasEnRango(LocalDate inicio, LocalDate fin) {
        return reservaRepo.findByFechaBetween(
                inicio.atStartOfDay(),
                fin.plusDays(1).atStartOfDay().minusNanos(1)
        );
    }

    private List<Pago> pagosEnRango(LocalDate inicio, LocalDate fin) {
        return pagoRepo.findByFechaPagoBetween(
                inicio.atStartOfDay(),
                fin.plusDays(1).atStartOfDay().minusNanos(1)
        );
    }

    private int horasReserva(Reserva reserva) {
        if (reserva.getHoraInicio() == null || reserva.getHoraFin() == null) {
            return 1;
        }
        return Math.max(1, (int) ChronoUnit.HOURS.between(reserva.getHoraInicio(), reserva.getHoraFin()));
    }
}
