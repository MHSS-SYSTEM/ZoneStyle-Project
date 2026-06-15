package com.estudiomusical.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer idEquipo;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 50)
    private String marca;

    @Column(length = 50)
    private String modelo;

    @Column(nullable = false, length = 30)
    private String estado; // DISPONIBLE, PRESTADO, MANTENIMIENTO
}
