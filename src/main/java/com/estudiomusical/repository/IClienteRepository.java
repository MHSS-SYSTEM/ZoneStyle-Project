package com.estudiomusical.repository;

import com.estudiomusical.model.Cliente;

import java.util.Optional;

public interface IClienteRepository extends IGenericRepository<Cliente, Integer> {
    Optional<Cliente> findByEmail(String email);
}
