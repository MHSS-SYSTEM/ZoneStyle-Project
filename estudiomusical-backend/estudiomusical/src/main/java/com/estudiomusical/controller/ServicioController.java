package com.estudiomusical.controller;

import com.estudiomusical.model.Servicio;
import com.estudiomusical.service.IServicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/servicios")
// @CrossOrigin(origins = "*")
public class ServicioController {
    private final IServicioService service;

    @GetMapping
    public ResponseEntity<List<Servicio>> findAll() throws Exception {
        List<Servicio> list = service.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Servicio> findById(@PathVariable Integer id) throws Exception {
        Servicio obj = service.findById(id);
        return ResponseEntity.ok(obj);
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody Servicio servicio) throws Exception {
        Servicio obj = service.save(servicio);

        //return new ResponseEntity<>(obj, HttpStatus.CREATED);
        //localhost:8080/servicios/1
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdServicio()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Servicio> update(@PathVariable Integer id, @RequestBody Servicio servicio) throws Exception {
        Servicio obj = service.update(servicio, id);
        return ResponseEntity.ok(obj);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws Exception {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
