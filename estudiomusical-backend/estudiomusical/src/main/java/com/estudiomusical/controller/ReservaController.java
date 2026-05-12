package com.estudiomusical.controller;

import com.estudiomusical.model.Reserva;
import com.estudiomusical.service.IReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservas")
@CrossOrigin(origins = "*")
public class ReservaController {

    private final IReservaService service;

    @GetMapping
    public List<Reserva> findAll() throws Exception {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Reserva findById(@PathVariable("id") Integer id) throws Exception {
        return service.findById(id);
    }

    @PostMapping
    public Reserva save(@RequestBody Reserva reserva) throws Exception {
        return service.save(reserva);
    }

    @PutMapping("/{id}")
    public Reserva update(@PathVariable("id") Integer id, @RequestBody Reserva reserva) throws Exception {
        return service.update(reserva, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) throws Exception {
        service.delete(id);
    }
}
