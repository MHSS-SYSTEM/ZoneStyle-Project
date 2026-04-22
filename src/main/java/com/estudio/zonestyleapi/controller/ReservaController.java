package com.estudio.zonestyleapi.controller;

import com.estudio.zonestyleapi.model.Reserva;
import com.estudio.zonestyleapi.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
public class ReservaController {
    @Autowired
    private ReservaService service;

    @GetMapping
    public List<Reserva> listar() {
        return service.listarTodos(); // Nombre sincronizado
    }

    @PostMapping
    public Reserva guardar(@RequestBody Reserva r) {
        return service.guardar(r);
    }
}