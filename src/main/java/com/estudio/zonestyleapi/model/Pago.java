package com.estudio.zonestyleapi.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double monto;
    private String metodoPago; // Ejemplo: Yape, Efectivo, Transferencia

    @OneToOne
    @JoinColumn(name = "reserva_id")
    private Reserva reserva; // Relaciona el pago con una reserva específica
}