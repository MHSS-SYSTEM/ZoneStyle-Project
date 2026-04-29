package com.upn.avionapi.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "aviones")
@Data // Genera getters, setters, toString y constructor por defecto
public class Avion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String modelo;
    private String aerolinea;
    private int capacidad;
    private double alcance;
    private boolean envuelo;
}