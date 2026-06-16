package com.estudiomusical.controller;

import com.estudiomusical.dto.PagoDTO;
import com.estudiomusical.model.Pago;
import com.estudiomusical.service.IPagoService;
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
@RequestMapping("/pagos")
public class PagoController {

    private final IPagoService service;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<PagoDTO>> findAll() throws Exception {
        List<PagoDTO> list = service.findAll().stream()
                .map(pago -> modelMapper.map(pago, PagoDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PagoDTO>> findById(@PathVariable Integer id) throws Exception {
        Pago obj = service.findById(id);
        PagoDTO dto = modelMapper.map(obj, PagoDTO.class);

        EntityModel<PagoDTO> resource = EntityModel.of(dto);

        WebMvcLinkBuilder linkToSelf = linkTo(methodOn(this.getClass()).findById(id));
        resource.add(linkToSelf.withSelfRel());

        WebMvcLinkBuilder linkToAll = linkTo(methodOn(this.getClass()).findAll());
        resource.add(linkToAll.withRel("all-pagos"));

        return ResponseEntity.ok(resource);
    }

    @GetMapping("/reserva/{idReserva}")
    public ResponseEntity<List<PagoDTO>> findByReserva(@PathVariable Integer idReserva) throws Exception {
        List<PagoDTO> list = service.findByReserva(idReserva).stream()
                .map(pago -> modelMapper.map(pago, PagoDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody PagoDTO dto) throws Exception {
        Pago pago = modelMapper.map(dto, Pago.class);
        Pago obj = service.save(pago);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(obj.getIdPago()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PostMapping("/reserva/{idReserva}")
    public ResponseEntity<PagoDTO> registrarPagoReserva(@PathVariable Integer idReserva, @Valid @RequestBody PagoDTO dto) throws Exception {
        Pago pago = modelMapper.map(dto, Pago.class);
        Pago obj = service.registrarPagoReserva(idReserva, pago);
        return ResponseEntity.ok(modelMapper.map(obj, PagoDTO.class));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagoDTO> update(@PathVariable Integer id, @Valid @RequestBody PagoDTO dto) throws Exception {
        Pago pago = modelMapper.map(dto, Pago.class);
        Pago obj = service.update(pago, id);
        PagoDTO resultDto = modelMapper.map(obj, PagoDTO.class);
        return ResponseEntity.ok(resultDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws Exception {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
