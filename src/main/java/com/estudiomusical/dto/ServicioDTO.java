package com.estudiomusical.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicioDTO {
    private Integer idServicio;

    @NotBlank(message = "El nombre del servicio es obligatorio")
    @Size(min = 3, max = 80, message = "El nombre del servicio debe tener entre 3 y 80 caracteres")
    private String nombre;

    @NotNull(message = "El precio por hora es obligatorio")
    @DecimalMin(value = "0.10", message = "El precio por hora debe ser mayor a cero")
    private Double precioPorHora;
}
