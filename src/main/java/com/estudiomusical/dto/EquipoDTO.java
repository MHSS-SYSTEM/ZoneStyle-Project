package com.estudiomusical.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipoDTO {
    private Integer idEquipo;

    @NotBlank(message = "El nombre del equipo es obligatorio")
    @Size(min = 2, max = 80, message = "El nombre del equipo debe tener entre 2 y 80 caracteres")
    private String nombre;

    @NotBlank(message = "La marca del equipo es obligatoria")
    @Size(max = 80, message = "La marca no debe superar 80 caracteres")
    private String marca;

    @NotBlank(message = "El modelo del equipo es obligatorio")
    @Size(max = 80, message = "El modelo no debe superar 80 caracteres")
    private String modelo;

    @NotBlank(message = "El estado del equipo es obligatorio")
    @Size(max = 30, message = "El estado no debe superar 30 caracteres")
    private String estado;
}
