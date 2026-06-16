package com.estudiomusical.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagoDTO {
    private Integer idPago;

    private ReservaDTO reserva;

    private LocalDateTime fechaPago;

    @NotNull(message = "El monto del pago es obligatorio")
    @DecimalMin(value = "0.10", message = "El monto del pago debe ser mayor a cero")
    private Double monto;

    @NotBlank(message = "El metodo de pago es obligatorio")
    @Size(max = 40, message = "El metodo de pago no debe superar 40 caracteres")
    private String metodoPago;

    @NotBlank(message = "El tipo de pago es obligatorio")
    @Size(max = 40, message = "El tipo de pago no debe superar 40 caracteres")
    private String tipoPago;
}
