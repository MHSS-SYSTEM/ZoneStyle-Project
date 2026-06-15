package com.estudiomusical.controller;

import com.estudiomusical.dto.reporte.ClienteFrecuenteDTO;
import com.estudiomusical.dto.reporte.EquipoUsadoDTO;
import com.estudiomusical.dto.reporte.IngresoPorFechaDTO;
import com.estudiomusical.dto.reporte.OcupacionSalaDTO;
import com.estudiomusical.dto.reporte.PagoPendienteDTO;
import com.estudiomusical.dto.reporte.ReporteResumenDTO;
import com.estudiomusical.dto.reporte.ReservasPorFechaDTO;
import com.estudiomusical.dto.reporte.ServicioSolicitadoDTO;
import com.estudiomusical.service.implementation.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reportes")
public class ReporteController {

    private final ReporteService service;

    @GetMapping("/resumen")
    public ResponseEntity<ReporteResumenDTO> resumen(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        RangoFechas rango = rango(inicio, fin);
        return ResponseEntity.ok(service.resumen(rango.inicio(), rango.fin()));
    }

    @GetMapping("/ingresos")
    public ResponseEntity<List<IngresoPorFechaDTO>> ingresos(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        RangoFechas rango = rango(inicio, fin);
        return ResponseEntity.ok(service.ingresosPorFecha(rango.inicio(), rango.fin()));
    }

    @GetMapping("/reservas")
    public ResponseEntity<List<ReservasPorFechaDTO>> reservas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        RangoFechas rango = rango(inicio, fin);
        return ResponseEntity.ok(service.reservasPorFecha(rango.inicio(), rango.fin()));
    }

    @GetMapping("/pagos-pendientes")
    public ResponseEntity<List<PagoPendienteDTO>> pagosPendientes() throws Exception {
        return ResponseEntity.ok(service.pagosPendientes());
    }

    @GetMapping("/servicios-mas-solicitados")
    public ResponseEntity<List<ServicioSolicitadoDTO>> serviciosMasSolicitados(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) throws Exception {
        RangoFechas rango = rango(inicio, fin);
        return ResponseEntity.ok(service.serviciosMasSolicitados(rango.inicio(), rango.fin()));
    }

    @GetMapping("/ocupacion-salas")
    public ResponseEntity<List<OcupacionSalaDTO>> ocupacionSalas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        RangoFechas rango = rango(inicio, fin);
        return ResponseEntity.ok(service.ocupacionSalas(rango.inicio(), rango.fin()));
    }

    @GetMapping("/ingresos-por-servicio")
    public ResponseEntity<List<ServicioSolicitadoDTO>> ingresosPorServicio(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) throws Exception {
        RangoFechas rango = rango(inicio, fin);
        return ResponseEntity.ok(service.ingresosPorServicio(rango.inicio(), rango.fin()));
    }

    @GetMapping("/clientes-frecuentes")
    public ResponseEntity<List<ClienteFrecuenteDTO>> clientesFrecuentes(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        RangoFechas rango = rango(inicio, fin);
        return ResponseEntity.ok(service.clientesFrecuentes(rango.inicio(), rango.fin()));
    }

    @GetMapping("/equipos-mas-usados")
    public ResponseEntity<List<EquipoUsadoDTO>> equiposMasUsados(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        RangoFechas rango = rango(inicio, fin);
        return ResponseEntity.ok(service.equiposMasUsados(rango.inicio(), rango.fin()));
    }

    private RangoFechas rango(LocalDate inicio, LocalDate fin) {
        LocalDate hoy = LocalDate.now();
        LocalDate inicioFinal = inicio != null ? inicio : hoy.withDayOfMonth(1);
        LocalDate finFinal = fin != null ? fin : hoy;
        return new RangoFechas(inicioFinal, finFinal);
    }

    private record RangoFechas(LocalDate inicio, LocalDate fin) {
    }
}
