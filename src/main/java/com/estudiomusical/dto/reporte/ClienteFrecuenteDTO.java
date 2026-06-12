package com.estudiomusical.dto.reporte;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteFrecuenteDTO {
    private Integer idCliente;
    private String cliente;
    private Long reservas;
    private Double totalGastado;
}
