package com.upn.relojapi.repository;

import com.upn.relojapi.model.Reloj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelojRepository extends JpaRepository<Reloj, Long> {
}