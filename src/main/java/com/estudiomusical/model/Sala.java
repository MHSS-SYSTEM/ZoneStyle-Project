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
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer idSala;

    @Column(nullable = false, length = 80)
    private String nombre;

    @Column(nullable = false)
    private boolean estado;
}
