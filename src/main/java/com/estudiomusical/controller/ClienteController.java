package com.estudiomusical.controller;

import com.estudiomusical.dto.ClienteDTO;
import com.estudiomusical.model.Cliente;
import com.estudiomusical.service.IClienteService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clientes")
public class ClienteController {

    private final IClienteService service;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> findAll() throws Exception {
        List<ClienteDTO> list = service.findAll().stream()
                .map(cliente -> modelMapper.map(cliente, ClienteDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ClienteDTO>> findById(@PathVariable Integer id) throws Exception {
        Cliente obj = service.findById(id);
        ClienteDTO dto = modelMapper.map(obj, ClienteDTO.class);

        // HATEOAS (Nivel 3)
        EntityModel<ClienteDTO> resource = EntityModel.of(dto);
        
        // Link al recurso individual (Self link)
        WebMvcLinkBuilder linkToSelf = linkTo(methodOn(this.getClass()).findById(id));
        resource.add(linkToSelf.withSelfRel());

        // Link al recurso coleccion (All clients link)
        WebMvcLinkBuilder linkToAll = linkTo(methodOn(this.getClass()).findAll());
        resource.add(linkToAll.withRel("all-clients"));

        return ResponseEntity.ok(resource);
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody ClienteDTO dto) throws Exception {
        Cliente cliente = modelMapper.map(dto, Cliente.class);
        Cliente obj = service.save(cliente);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(obj.getIdCliente()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> update(@PathVariable Integer id, @RequestBody ClienteDTO dto) throws Exception {
        Cliente cliente = modelMapper.map(dto, Cliente.class);
        Cliente obj = service.update(cliente, id);
        ClienteDTO resultDto = modelMapper.map(obj, ClienteDTO.class);
        return ResponseEntity.ok(resultDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws Exception {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
