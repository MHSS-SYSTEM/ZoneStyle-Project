package com.estudiomusical.dto;

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
    private Double monto;
    private String metodoPago;
    private String tipoPago;
}
