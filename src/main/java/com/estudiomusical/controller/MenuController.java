package com.estudiomusical.controller;

import com.estudiomusical.model.Menu;
import com.estudiomusical.service.IMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/menus")
@RequiredArgsConstructor
public class MenuController {
    private final IMenuService service;

    @GetMapping("/user")
    public ResponseEntity<List<Menu>> getMenusByUser() {
        return ResponseEntity.ok(service.getMenusByUsername());
    }
}
