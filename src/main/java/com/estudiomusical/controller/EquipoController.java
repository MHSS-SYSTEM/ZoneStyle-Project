package com.estudiomusical.controller;

import com.estudiomusical.dto.EquipoDTO;
import com.estudiomusical.model.Equipo;
import com.estudiomusical.service.IEquipoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/equipos")
public class EquipoController {

    private final IEquipoService service;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<EquipoDTO>> findAll() throws Exception {
        List<EquipoDTO> list = service.findAll().stream()
                .map(equipo -> modelMapper.map(equipo, EquipoDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/pageable")
    public ResponseEntity<Page<EquipoDTO>> listPageable(Pageable pageable) throws Exception {
        Page<Equipo> page = service.listPage(pageable);
        Page<EquipoDTO> dtoPage = page.map(equipo -> modelMapper.map(equipo, EquipoDTO.class));
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<EquipoDTO>> findById(@PathVariable Integer id) throws Exception {
        Equipo obj = service.findById(id);
        EquipoDTO dto = modelMapper.map(obj, EquipoDTO.class);

        EntityModel<EquipoDTO> resource = EntityModel.of(dto);

        WebMvcLinkBuilder linkToSelf = linkTo(methodOn(this.getClass()).findById(id));
        resource.add(linkToSelf.withSelfRel());

        WebMvcLinkBuilder linkToAll = linkTo(methodOn(this.getClass()).findAll());
        resource.add(linkToAll.withRel("all-equipos"));

        return ResponseEntity.ok(resource);
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<EquipoDTO>> findDisponibles(
            @RequestParam LocalDate fecha,
            @RequestParam LocalTime horaInicio,
            @RequestParam LocalTime horaFin) throws Exception {
        List<EquipoDTO> list = service.findDisponibles(fecha, horaInicio, horaFin).stream()
                .map(equipo -> modelMapper.map(equipo, EquipoDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody EquipoDTO dto) throws Exception {
        Equipo equipo = modelMapper.map(dto, Equipo.class);
        Equipo obj = service.save(equipo);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(obj.getIdEquipo()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipoDTO> update(@PathVariable Integer id, @Valid @RequestBody EquipoDTO dto) throws Exception {
        Equipo equipo = modelMapper.map(dto, Equipo.class);
        Equipo obj = service.update(equipo, id);
        EquipoDTO resultDto = modelMapper.map(obj, EquipoDTO.class);
        return ResponseEntity.ok(resultDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws Exception {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}