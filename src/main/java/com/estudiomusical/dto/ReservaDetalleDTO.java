package com.estudiomusical.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaDetalleDTO {
    private Integer idReservaDetalle;
    
    @JsonIgnoreProperties("detalles")
    private ReservaDTO reserva;
    
    private ServicioDTO servicio;
    private Integer cantidadHoras;
    private Double subtotal;
}
