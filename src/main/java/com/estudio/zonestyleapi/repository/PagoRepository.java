package com.estudio.zonestyleapi.repository;

import com.estudio.zonestyleapi.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    // Aquí debe decir Pago
}