package com.estudiomusical.repository;

import com.estudiomusical.model.Sala;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISalaRepository extends JpaRepository<Sala, Integer> {
}
