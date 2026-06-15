package com.estudiomusical.dto.reporte;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReporteResumenDTO {
    private Long totalReservas;
    private Long totalClientes;
    private Double ingresos;
    private Double saldosPendientes;
}
