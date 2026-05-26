package com.estudiomusical.controller;

import com.estudiomusical.dto.ReservaDTO;
import com.estudiomusical.model.Reserva;
import com.estudiomusical.service.IReservaService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
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
    public ResponseEntity<ReservaDTO> findById(@PathVariable Integer id) throws Exception {
        Reserva obj = service.findById(id);
        ReservaDTO dto = modelMapper.map(obj, ReservaDTO.class);
        return ResponseEntity.ok(dto);
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
}
