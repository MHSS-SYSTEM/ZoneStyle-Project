package com.upn.avionapi.controller;

import com.upn.avionapi.model.Avion;
import com.upn.avionapi.service.IAvionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/aviones")
public class AvionController {

    @Autowired
    private IAvionService service;

    @GetMapping // Método GET para listar
    public List<Avion> listar() { return service.listarTodos(); }

    @PostMapping // Método POST para crear
    public Avion crear(@RequestBody Avion avion) { return service.guardar(avion); }

    @PutMapping("/{id}") // Método PUT para actualizar
    public Avion actualizar(@PathVariable Long id, @RequestBody Avion avion) {
        avion.setId(id);
        return service.guardar(avion);
    }

    @DeleteMapping("/{id}") // Método DELETE para eliminar
    public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}