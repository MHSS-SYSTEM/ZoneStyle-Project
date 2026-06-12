package com.estudiomusical.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipoDTO {
    private Integer idEquipo;
    private String nombre;
    private String marca;
    private String modelo;
    private String estado;
}
