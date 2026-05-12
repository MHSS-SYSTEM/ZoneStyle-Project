package com.estudiomusical.controller;

import com.estudiomusical.model.ReservaDetalle;
import com.estudiomusical.service.IReservaDetalleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reserva-detalles")
@CrossOrigin(origins = "*")
public class ReservaDetalleController {

    private final IReservaDetalleService service;

    @GetMapping
    public List<ReservaDetalle> findAll() throws Exception {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ReservaDetalle findById(@PathVariable("id") Integer id) throws Exception {
        return service.findById(id);
    }

    @PostMapping
    public ReservaDetalle save(@RequestBody ReservaDetalle reservaDetalle) throws Exception {
        return service.save(reservaDetalle);
    }

    @PutMapping("/{id}")
    public ReservaDetalle update(@PathVariable("id") Integer id, @RequestBody ReservaDetalle reservaDetalle) throws Exception {
        return service.update(reservaDetalle, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) throws Exception {
        service.delete(id);
    }
}
