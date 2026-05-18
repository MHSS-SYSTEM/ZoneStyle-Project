package com.estudiomusical.repository;

import com.estudiomusical.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IClienteRepository extends JpaRepository<Cliente, Integer> {
    Optional<Cliente> findByEmail(String email);

    boolean existsByEmail(String email);

}
