package com.estudiomusical.controller;

import com.estudiomusical.model.Servicio;
import com.estudiomusical.service.IServicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/servicios")
@CrossOrigin(origins = "*")
public class ServicioController {

    private final IServicioService service;

    @GetMapping
    public List<Servicio> findAll() throws Exception {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Servicio findById(@PathVariable("id") Integer id) throws Exception {
        return service.findById(id);
    }

    @PostMapping
    public Servicio save(@RequestBody Servicio servicio) throws Exception {
        return service.save(servicio);
    }

    @PutMapping("/{id}")
    public Servicio update(@PathVariable("id") Integer id, @RequestBody Servicio servicio) throws Exception {
        return service.update(servicio, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) throws Exception {
        service.delete(id);
    }
}
