package com.pisocompartido.backendtetoca.controller;

import com.pisocompartido.backendtetoca.dto.CrearPisoRequest;
import com.pisocompartido.backendtetoca.dto.PisoResponse;
import com.pisocompartido.backendtetoca.service.PisoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pisos")
public class PisoController {

    private final PisoService pisoService;

    public PisoController(PisoService pisoService) {
        this.pisoService = pisoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PisoResponse crear(@Valid @RequestBody CrearPisoRequest request) {
        return pisoService.crear(request);
    }

    @GetMapping("/{id}")
    public PisoResponse obtener(@PathVariable Long id) {
        return pisoService.obtener(id);
    }
}