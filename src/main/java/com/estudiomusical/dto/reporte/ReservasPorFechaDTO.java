package com.estudiomusical.dto.reporte;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservasPorFechaDTO {
    private LocalDate fecha;
    private Long cantidad;
}
