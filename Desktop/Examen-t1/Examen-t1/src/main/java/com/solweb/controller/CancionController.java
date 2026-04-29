package com.solweb.controller;

import com.solweb.model.Cancion;
import com.solweb.service.ICancionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/canciones")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CancionController {

    private final ICancionService service;

    @GetMapping
    public List<Cancion> listar() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Cancion> buscarPorId(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PostMapping
    public Cancion guardar(@RequestBody Cancion cancion) {
        return service.save(cancion);
    }

    @PutMapping("/{id}")
    public Cancion actualizar(@PathVariable Integer id, @RequestBody Cancion cancion) {
        return service.update(id, cancion);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        service.delete(id);
    }
}
