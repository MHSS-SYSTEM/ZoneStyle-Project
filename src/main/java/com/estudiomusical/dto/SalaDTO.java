package com.estudiomusical.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalaDTO {
    private Integer idSala;
    private String nombre;
    private Boolean estado;
}
