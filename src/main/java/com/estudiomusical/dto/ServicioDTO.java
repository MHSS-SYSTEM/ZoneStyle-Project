package com.estudiomusical.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicioDTO {
    private Integer idServicio;
    private String nombre;
    private Double precioPorHora;
}
