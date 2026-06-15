package com.estudiomusical.dto.reporte;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OcupacionSalaDTO {
    private Integer idSala;
    private String sala;
    private Long reservas;
    private Integer horasOcupadas;
}
