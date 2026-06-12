package com.estudiomusical.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaDTO {
    private Integer idReserva;
    private ClienteDTO cliente;
    private SalaDTO sala;
    private LocalDateTime fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Double total;
    private Double abono;
    private Double saldo;
    private String metodoPago;
    private String estado;
    private List<ReservaDetalleDTO> detalles;
    private List<EquipoDTO> equipos;
}
