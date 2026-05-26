package com.estudiomusical.controller;

import com.estudiomusical.dto.ServicioDTO;
import com.estudiomusical.model.Servicio;
import com.estudiomusical.service.IServicioService;
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
@RequestMapping("/servicios")
public class ServicioController {

    private final IServicioService service;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<ServicioDTO>> findAll() throws Exception {
        List<ServicioDTO> list = service.findAll().stream()
                .map(servicio -> modelMapper.map(servicio, ServicioDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioDTO> findById(@PathVariable Integer id) throws Exception {
        Servicio obj = service.findById(id);
        ServicioDTO dto = modelMapper.map(obj, ServicioDTO.class);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody ServicioDTO dto) throws Exception {
        Servicio servicio = modelMapper.map(dto, Servicio.class);
        Servicio obj = service.save(servicio);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(obj.getIdServicio()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicioDTO> update(@PathVariable Integer id, @RequestBody ServicioDTO dto) throws Exception {
        Servicio servicio = modelMapper.map(dto, Servicio.class);
        Servicio obj = service.update(servicio, id);
        ServicioDTO resultDto = modelMapper.map(obj, ServicioDTO.class);
        return ResponseEntity.ok(resultDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws Exception {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
