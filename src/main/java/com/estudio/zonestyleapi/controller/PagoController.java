package com.estudio.zonestyleapi.controller;

import com.estudio.zonestyleapi.model.Pago;
import com.estudio.zonestyleapi.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "*")
public class PagoController {
    @Autowired
    private PagoService service;

    @GetMapping
    public List<Pago> listar() {
        return service.listarTodos(); // Nombre sincronizado
    }

    @PostMapping
    public Pago guardar(@RequestBody Pago p) {
        return service.guardar(p);
    }
}