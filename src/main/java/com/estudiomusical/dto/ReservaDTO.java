package com.estudiomusical.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaDTO {
    private Integer idReserva;
    private ClienteDTO cliente;
    private SalaDTO sala;
    private LocalDateTime fecha;
    private Double total;
    private List<ReservaDetalleDTO> detalles;
}
