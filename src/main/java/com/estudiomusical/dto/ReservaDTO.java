package com.estudiomusical.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaDTO {
    private Integer idReserva;

    @Valid
    @NotNull(message = "El cliente es obligatorio")
    private ClienteDTO cliente;

    @Valid
    @NotNull(message = "La sala es obligatoria")
    private SalaDTO sala;

    @NotNull(message = "La fecha de la reserva es obligatoria")
    private LocalDateTime fecha;

    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria")
    private LocalTime horaFin;

    @NotNull(message = "El total es obligatorio")
    @DecimalMin(value = "0.10", message = "El total debe ser mayor a cero")
    private Double total;

    @NotNull(message = "El abono es obligatorio")
    @DecimalMin(value = "0.00", message = "El abono no puede ser negativo")
    private Double abono;

    @DecimalMin(value = "0.00", message = "El saldo no puede ser negativo")
    private Double saldo;

    @NotBlank(message = "El metodo de pago es obligatorio")
    @Size(max = 40, message = "El metodo de pago no debe superar 40 caracteres")
    private String metodoPago;

    @NotBlank(message = "El estado de la reserva es obligatorio")
    @Size(max = 30, message = "El estado no debe superar 30 caracteres")
    private String estado;

    @Valid
    @NotEmpty(message = "La reserva debe incluir al menos un servicio")
    private List<ReservaDetalleDTO> detalles;

    @Valid
    private List<EquipoDTO> equipos;
}
