package com.estudiomusical.dto.reporte;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicioSolicitadoDTO {
    private Integer idServicio;
    private String servicio;
    private Long cantidadReservas;
    private Integer horasVendidas;
    private Double totalGenerado;
}
