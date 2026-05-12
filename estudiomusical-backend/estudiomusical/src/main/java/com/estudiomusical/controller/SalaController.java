package com.estudiomusical.controller;

import com.estudiomusical.model.Sala;
import com.estudiomusical.service.ISalaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/salas")
@CrossOrigin(origins = "*")
public class SalaController {

    private final ISalaService service;

    @GetMapping
    public List<Sala> findAll() throws Exception {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Sala findById(@PathVariable("id") Integer id) throws Exception {
        return service.findById(id);
    }

    @PostMapping
    public Sala save(@RequestBody Sala sala) throws Exception {
        return service.save(sala);
    }

    @PutMapping("/{id}")
    public Sala update(@PathVariable("id") Integer id, @RequestBody Sala sala) throws Exception {
        return service.update(sala, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) throws Exception {
        service.delete(id);
    }
}
