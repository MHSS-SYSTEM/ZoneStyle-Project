package com.estudio.zonestyleapi.service;

import com.estudio.zonestyleapi.model.Cliente;
import com.estudio.zonestyleapi.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository repository;

    // Fíjate en este nombre:
    public List<Cliente> listarTodos() {
        return repository.findAll();
    }

    public Cliente guardar(Cliente c) {
        return repository.save(c);
    }
}