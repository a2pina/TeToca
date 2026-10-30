package com.pisocompartido.backendtetoca.controller;

import com.pisocompartido.backendtetoca.dto.AsignacionResponse;
import com.pisocompartido.backendtetoca.dto.CrearAsignacionRequest;
import com.pisocompartido.backendtetoca.model.EstadoAsignacion;
import com.pisocompartido.backendtetoca.service.AsignacionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AsignacionController {

    private final AsignacionService asignacionService;

    public AsignacionController(AsignacionService asignacionService) {
        this.asignacionService = asignacionService;
    }

    @PostMapping("/tareas/{tareaId}/asignaciones")
    @ResponseStatus(HttpStatus.CREATED)
    public AsignacionResponse asignar(@PathVariable Long tareaId,
                                      @Valid @RequestBody CrearAsignacionRequest request) {
        return asignacionService.asignar(tareaId, request);
    }

    @PatchMapping("/asignaciones/{id}/completar")
    public AsignacionResponse completar(@PathVariable Long id) {
        return asignacionService.completar(id);
    }

    @GetMapping("/pisos/{pisoId}/asignaciones")
    public List<AsignacionResponse> listar(@PathVariable Long pisoId,
                                           @RequestParam(required = false) EstadoAsignacion estado) {
        return asignacionService.listar(pisoId, estado);
    }
}