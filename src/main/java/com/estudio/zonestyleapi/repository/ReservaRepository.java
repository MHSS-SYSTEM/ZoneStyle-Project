package com.estudio.zonestyleapi.repository;

import com.estudio.zonestyleapi.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepository extends JpaRepository<Cliente, Long> {
}