package com.upn.relojapi.controller;

import com.upn.relojapi.model.Reloj;
import com.upn.relojapi.repository.RelojRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/relojes")
public class RelojController {

    @Autowired
    private RelojRepository repository;

    @GetMapping
    public List<Reloj> listar() {
        return repository.findAll();
    }

    @PostMapping
    public Reloj crear(@RequestBody Reloj reloj) {
        return repository.save(reloj);
    }
} // Asegúrate de que esta última llave exista