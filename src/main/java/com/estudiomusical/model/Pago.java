package com.estudiomusical.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer idPago;

    @ManyToOne
    @JoinColumn(name = "id_reserva", nullable = false, foreignKey = @ForeignKey(name = "FK_PAGO_RESERVA"))
    private Reserva reserva;

    @Column(nullable = false)
    private LocalDateTime fechaPago;

    @Column(nullable = false)
    private Double monto;

    @Column(nullable = false, length = 50)
    private String metodoPago;

    @Column(nullable = false, length = 50)
    private String tipoPago;
}
