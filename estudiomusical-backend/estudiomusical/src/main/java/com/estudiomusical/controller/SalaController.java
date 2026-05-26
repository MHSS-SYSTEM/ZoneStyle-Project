package com.estudiomusical.controller;

import com.estudiomusical.model.Sala;
import com.estudiomusical.service.ISalaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/salas")
// @CrossOrigin(origins = "*")
public class SalaController {
    private final ISalaService service;

    @GetMapping
    public ResponseEntity<List<Sala>> findAll() throws Exception {
        List<Sala> list = service.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sala> findById(@PathVariable Integer id) throws Exception {
        Sala obj = service.findById(id);
        return ResponseEntity.ok(obj);
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody Sala sala) throws Exception {
        Sala obj = service.save(sala);

        //return new ResponseEntity<>(obj, HttpStatus.CREATED);
        //localhost:8080/salas/1
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdSala()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sala> update(@PathVariable Integer id, @RequestBody Sala sala) throws Exception {
        Sala obj = service.update(sala, id);
        return ResponseEntity.ok(obj);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws Exception {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
