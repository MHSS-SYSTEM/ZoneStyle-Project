package com.estudiomusical.controller;

import com.estudiomusical.dto.ReservaDTO;
import com.estudiomusical.model.Reserva;
import com.estudiomusical.service.IReservaService;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservas")
public class ReservaController {

    private final IReservaService service;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<ReservaDTO>> findAll() throws Exception {
        List<ReservaDTO> list = service.findAll().stream()
                .map(reserva -> modelMapper.map(reserva, ReservaDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ReservaDTO>> findById(@PathVariable Integer id) throws Exception {
        Reserva obj = service.findById(id);
        ReservaDTO dto = modelMapper.map(obj, ReservaDTO.class);

        EntityModel<ReservaDTO> resource = EntityModel.of(dto);

        WebMvcLinkBuilder linkToSelf = linkTo(methodOn(this.getClass()).findById(id));
        resource.add(linkToSelf.withSelfRel());

        WebMvcLinkBuilder linkToAll = linkTo(methodOn(this.getClass()).findAll());
        resource.add(linkToAll.withRel("all-reservas"));

        return ResponseEntity.ok(resource);
    }

    @GetMapping("/disponibilidad")
    public ResponseEntity<Map<String, Boolean>> disponibilidad(
            @RequestParam Integer salaId,
            @RequestParam LocalDate fecha,
            @RequestParam LocalTime horaInicio,
            @RequestParam LocalTime horaFin,
            @RequestParam(required = false) Integer excluirReservaId) throws Exception {
        boolean disponible = service.existeDisponibilidad(salaId, fecha, horaInicio, horaFin, excluirReservaId);
        return ResponseEntity.ok(Map.of("disponible", disponible));
    }

    @GetMapping("/por-fecha")
    public ResponseEntity<List<ReservaDTO>> findByFecha(@RequestParam LocalDate inicio, @RequestParam LocalDate fin) throws Exception {
        List<ReservaDTO> list = service.findByFecha(inicio, fin).stream()
                .map(reserva -> modelMapper.map(reserva, ReservaDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<ReservaDTO>> findByCliente(@PathVariable Integer idCliente) throws Exception {
        List<ReservaDTO> list = service.findByCliente(idCliente).stream()
                .map(reserva -> modelMapper.map(reserva, ReservaDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/sala/{idSala}")
    public ResponseEntity<List<ReservaDTO>> findBySala(@PathVariable Integer idSala) throws Exception {
        List<ReservaDTO> list = service.findBySala(idSala).stream()
                .map(reserva -> modelMapper.map(reserva, ReservaDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ReservaDTO>> findByEstado(@PathVariable String estado) throws Exception {
        List<ReservaDTO> list = service.findByEstado(estado).stream()
                .map(reserva -> modelMapper.map(reserva, ReservaDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/pendientes-pago")
    public ResponseEntity<List<ReservaDTO>> findPendientesPago() throws Exception {
        List<ReservaDTO> list = service.findPendientesPago().stream()
                .map(reserva -> modelMapper.map(reserva, ReservaDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody ReservaDTO dto) throws Exception {
        Reserva reserva = modelMapper.map(dto, Reserva.class);
        
        // Asignar bidireccionalmente la relación reserva en los detalles
        if (reserva.getDetalles() != null) {
            reserva.getDetalles().forEach(detalle -> detalle.setReserva(reserva));
        }
        
        Reserva obj = service.save(reserva);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(obj.getIdReserva()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservaDTO> update(@PathVariable Integer id, @RequestBody ReservaDTO dto) throws Exception {
        Reserva reserva = modelMapper.map(dto, Reserva.class);
        
        // Asignar bidireccionalmente la relación reserva en los detalles
        if (reserva.getDetalles() != null) {
            reserva.getDetalles().forEach(detalle -> detalle.setReserva(reserva));
        }

        Reserva obj = service.update(reserva, id);
        ReservaDTO resultDto = modelMapper.map(obj, ReservaDTO.class);
        return ResponseEntity.ok(resultDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws Exception {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ReservaDTO> cambiarEstado(@PathVariable Integer id, @RequestParam String estado) throws Exception {
        Reserva obj = service.cambiarEstado(id, estado);
        return ResponseEntity.ok(modelMapper.map(obj, ReservaDTO.class));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<ReservaDTO> cancelar(@PathVariable Integer id) throws Exception {
        Reserva obj = service.cancelar(id);
        return ResponseEntity.ok(modelMapper.map(obj, ReservaDTO.class));
    }
}
