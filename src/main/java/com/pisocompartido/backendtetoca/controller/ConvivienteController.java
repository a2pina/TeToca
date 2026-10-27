package com.pisocompartido.backendtetoca.controller;

import com.pisocompartido.backendtetoca.dto.ConvivienteResponse;
import com.pisocompartido.backendtetoca.dto.CrearConvivienteRequest;
import com.pisocompartido.backendtetoca.service.ConvivienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pisos/{pisoId}/convivientes")
public class ConvivienteController {

    private final ConvivienteService convivienteService;

    public ConvivienteController(ConvivienteService convivienteService) {
        this.convivienteService = convivienteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConvivienteResponse crear(@PathVariable Long pisoId,
                                     @Valid @RequestBody CrearConvivienteRequest request) {
        return convivienteService.crear(pisoId, request);
    }

    @GetMapping
    public List<ConvivienteResponse> listar(@PathVariable Long pisoId) {
        return convivienteService.listar(pisoId);
    }
}
