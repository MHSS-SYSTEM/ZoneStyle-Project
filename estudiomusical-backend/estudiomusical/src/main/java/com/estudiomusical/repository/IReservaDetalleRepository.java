package com.estudiomusical.repository;

import com.estudiomusical.model.ReservaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IReservaDetalleRepository extends JpaRepository<ReservaDetalle, Integer> {
}
