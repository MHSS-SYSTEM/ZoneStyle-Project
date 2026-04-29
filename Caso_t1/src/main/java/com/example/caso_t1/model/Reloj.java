package com.upn.relojapi.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "relojes")
public class Reloj {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String marca;
    private Double precio;
}