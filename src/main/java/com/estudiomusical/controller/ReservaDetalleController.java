package com.estudiomusical.controller;

import com.estudiomusical.dto.ReservaDetalleDTO;
import com.estudiomusical.model.ReservaDetalle;
import com.estudiomusical.service.IReservaDetalleService;
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
@RequestMapping("/reserva-detalles")
public class ReservaDetalleController {

    private final IReservaDetalleService service;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<ReservaDetalleDTO>> findAll() throws Exception {
        List<ReservaDetalleDTO> list = service.findAll().stream()
                .map(detalle -> modelMapper.map(detalle, ReservaDetalleDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ReservaDetalleDTO>> findById(@PathVariable Integer id) throws Exception {
        ReservaDetalle obj = service.findById(id);
        ReservaDetalleDTO dto = modelMapper.map(obj, ReservaDetalleDTO.class);

        EntityModel<ReservaDetalleDTO> resource = EntityModel.of(dto);

        WebMvcLinkBuilder linkToSelf = linkTo(methodOn(this.getClass()).findById(id));
        resource.add(linkToSelf.withSelfRel());

        WebMvcLinkBuilder linkToAll = linkTo(methodOn(this.getClass()).findAll());
        resource.add(linkToAll.withRel("all-reserva-detalles"));

        return ResponseEntity.ok(resource);
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody ReservaDetalleDTO dto) throws Exception {
        ReservaDetalle reservaDetalle = modelMapper.map(dto, ReservaDetalle.class);
        ReservaDetalle obj = service.save(reservaDetalle);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(obj.getIdReservaDetalle()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservaDetalleDTO> update(@PathVariable Integer id, @RequestBody ReservaDetalleDTO dto) throws Exception {
        ReservaDetalle reservaDetalle = modelMapper.map(dto, ReservaDetalle.class);
        ReservaDetalle obj = service.update(reservaDetalle, id);
        ReservaDetalleDTO resultDto = modelMapper.map(obj, ReservaDetalleDTO.class);
        return ResponseEntity.ok(resultDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws Exception {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
