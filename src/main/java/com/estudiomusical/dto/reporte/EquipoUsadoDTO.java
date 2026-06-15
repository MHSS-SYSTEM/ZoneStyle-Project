package com.estudiomusical.dto.reporte;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipoUsadoDTO {
    private Integer idEquipo;
    private String equipo;
    private Long reservas;
}
