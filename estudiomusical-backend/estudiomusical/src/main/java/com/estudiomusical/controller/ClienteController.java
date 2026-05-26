package com.estudiomusical.controller;

import com.estudiomusical.model.Cliente;
import com.estudiomusical.service.IClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clientes")
// @CrossOrigin(origins = "*")
public class ClienteController {
    private final IClienteService service;

    @GetMapping
    public ResponseEntity<List<Cliente>> findAll() throws Exception {
        List<Cliente> list = service.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> findById(@PathVariable Integer id) throws Exception {
        Cliente obj = service.findById(id);
        return ResponseEntity.ok(obj);
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody Cliente cliente) throws Exception {
        Cliente obj = service.save(cliente);

        //return new ResponseEntity<>(obj, HttpStatus.CREATED);
        //localhost:8080/clientes/1
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdCliente()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> update(@PathVariable Integer id, @RequestBody Cliente cliente) throws Exception {
        Cliente obj = service.update(cliente, id);
        return ResponseEntity.ok(obj);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws Exception {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
