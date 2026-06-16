package com.estudiomusical.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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

    @Valid
    @NotNull(message = "El servicio del detalle es obligatorio")
    private ServicioDTO servicio;

    @NotNull(message = "La cantidad de horas es obligatoria")
    @Min(value = 1, message = "La cantidad de horas debe ser al menos 1")
    private Integer cantidadHoras;

    @NotNull(message = "El subtotal es obligatorio")
    @DecimalMin(value = "0.10", message = "El subtotal debe ser mayor a cero")
    private Double subtotal;
}
