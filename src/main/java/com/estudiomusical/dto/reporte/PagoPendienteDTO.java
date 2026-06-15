package com.estudiomusical.dto.reporte;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagoPendienteDTO {
    private Integer idReserva;
    private String cliente;
    private String sala;
    private LocalDateTime fecha;
    private Double total;
    private Double abonado;
    private Double saldo;
}
