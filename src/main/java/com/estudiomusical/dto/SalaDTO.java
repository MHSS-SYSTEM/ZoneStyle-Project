package com.estudiomusical.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalaDTO {
    private Integer idSala;

    @NotBlank(message = "El nombre de la sala es obligatorio")
    @Size(min = 2, max = 80, message = "El nombre de la sala debe tener entre 2 y 80 caracteres")
    private String nombre;

    @NotNull(message = "El estado de la sala es obligatorio")
    private Boolean estado;
}
