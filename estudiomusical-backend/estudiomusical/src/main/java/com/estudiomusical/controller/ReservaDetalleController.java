package com.estudiomusical.controller;

import com.estudiomusical.model.ReservaDetalle;
import com.estudiomusical.service.IReservaDetalleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reserva-detalles")
// @CrossOrigin(origins = "*")
public class ReservaDetalleController {
    private final IReservaDetalleService service;

    @GetMapping
    public ResponseEntity<List<ReservaDetalle>> findAll() throws Exception {
        List<ReservaDetalle> list = service.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaDetalle> findById(@PathVariable Integer id) throws Exception {
        ReservaDetalle obj = service.findById(id);
        return ResponseEntity.ok(obj);
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody ReservaDetalle reservaDetalle) throws Exception {
        ReservaDetalle obj = service.save(reservaDetalle);

        //return new ResponseEntity<>(obj, HttpStatus.CREATED);
        //localhost:8080/reserva-detalles/1
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdReservaDetalle()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservaDetalle> update(@PathVariable Integer id, @RequestBody ReservaDetalle reservaDetalle) throws Exception {
        ReservaDetalle obj = service.update(reservaDetalle, id);
        return ResponseEntity.ok(obj);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws Exception {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
