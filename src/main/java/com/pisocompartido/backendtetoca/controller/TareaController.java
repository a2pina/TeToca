package com.pisocompartido.backendtetoca.controller;

import com.pisocompartido.backendtetoca.dto.CrearTareaRequest;
import com.pisocompartido.backendtetoca.dto.TareaResponse;
import com.pisocompartido.backendtetoca.service.TareaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pisos/{pisoId}/tareas")
public class TareaController {

    private final TareaService tareaService;

    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TareaResponse crear(@PathVariable Long pisoId,
                               @Valid @RequestBody CrearTareaRequest request) {
        return tareaService.crear(pisoId, request);
    }

    @GetMapping
    public List<TareaResponse> listar(@PathVariable Long pisoId) {
        return tareaService.listar(pisoId);
    }
}
