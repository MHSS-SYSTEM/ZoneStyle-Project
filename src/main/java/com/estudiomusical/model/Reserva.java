package com.estudiomusical.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer idReserva;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false, foreignKey = @ForeignKey(name = "FK_RESERVA_CLIENTE"))
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_sala", nullable = false, foreignKey = @ForeignKey(name = "FK_RESERVA_SALA"))
    private Sala sala;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column
    private LocalTime horaInicio;

    @Column
    private LocalTime horaFin;

    @Column(nullable = false)
    private Double total;

    @Column(nullable = false)
    private Double abono;

    @Column(nullable = false)
    private Double saldo;

    @Column(length = 50)
    private String metodoPago;

    @Column(length = 30)
    private String estado;

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservaDetalle> detalles;

    @ManyToMany
    @JoinTable(
            name = "reserva_equipo",
            joinColumns = @JoinColumn(name = "id_reserva", referencedColumnName = "idReserva"),
            inverseJoinColumns = @JoinColumn(name = "id_equipo", referencedColumnName = "idEquipo")
    )
    private List<Equipo> equipos;
}
