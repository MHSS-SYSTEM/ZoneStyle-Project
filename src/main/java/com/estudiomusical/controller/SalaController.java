package com.estudiomusical.controller;

import com.estudiomusical.dto.SalaDTO;
import com.estudiomusical.model.Sala;
import com.estudiomusical.service.ISalaService;
import jakarta.validation.Valid;
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
@RequestMapping("/salas")
public class SalaController {

    private final ISalaService service;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<SalaDTO>> findAll() throws Exception {
        List<SalaDTO> list = service.findAll().stream()
                .map(sala -> modelMapper.map(sala, SalaDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<SalaDTO>> findById(@PathVariable Integer id) throws Exception {
        Sala obj = service.findById(id);
        SalaDTO dto = modelMapper.map(obj, SalaDTO.class);

        EntityModel<SalaDTO> resource = EntityModel.of(dto);

        WebMvcLinkBuilder linkToSelf = linkTo(methodOn(this.getClass()).findById(id));
        resource.add(linkToSelf.withSelfRel());

        WebMvcLinkBuilder linkToAll = linkTo(methodOn(this.getClass()).findAll());
        resource.add(linkToAll.withRel("all-salas"));

        return ResponseEntity.ok(resource);
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody SalaDTO dto) throws Exception {
        Sala sala = modelMapper.map(dto, Sala.class);
        Sala obj = service.save(sala);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(obj.getIdSala()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalaDTO> update(@PathVariable Integer id, @Valid @RequestBody SalaDTO dto) throws Exception {
        Sala sala = modelMapper.map(dto, Sala.class);
        Sala obj = service.update(sala, id);
        SalaDTO resultDto = modelMapper.map(obj, SalaDTO.class);
        return ResponseEntity.ok(resultDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws Exception {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
