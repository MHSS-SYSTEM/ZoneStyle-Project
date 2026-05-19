package com.estudiomusical.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class ReservaDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer idReservaDetalle;

    // Lado inverso de la relacion JSON para no serializar Reserva en bucle.
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_reserva", nullable = false, foreignKey = @ForeignKey(name = "FK_DETALLE_RESERVA"))
    private Reserva reserva;

    @ManyToOne
    @JoinColumn(name = "id_servicio", nullable = false, foreignKey = @ForeignKey(name = "FK_DETALLE_SERVICIO"))
    private Servicio servicio;

    @Column(nullable = false)
    private Integer cantidadHoras;

    @Column(nullable = false)
    private Double subtotal;
}
