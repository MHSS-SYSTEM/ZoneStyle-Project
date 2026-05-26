package com.estudiomusical.controller;

import com.estudiomusical.model.Reserva;
import com.estudiomusical.service.IReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservas")
// @CrossOrigin(origins = "*")
public class ReservaController {
    private final IReservaService service;

    @GetMapping
    public ResponseEntity<List<Reserva>> findAll() throws Exception {
        List<Reserva> list = service.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> findById(@PathVariable Integer id) throws Exception {
        Reserva obj = service.findById(id);
        return ResponseEntity.ok(obj);
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody Reserva reserva) throws Exception {
        Reserva obj = service.save(reserva);

        //return new ResponseEntity<>(obj, HttpStatus.CREATED);
        //localhost:8080/reservas/1
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdReserva()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reserva> update(@PathVariable Integer id, @RequestBody Reserva reserva) throws Exception {
        Reserva obj = service.update(reserva, id);
        return ResponseEntity.ok(obj);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws Exception {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
