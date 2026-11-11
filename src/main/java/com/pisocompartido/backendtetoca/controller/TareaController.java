package com.pisocompartido.backendtetoca.controller;

import com.pisocompartido.backendtetoca.dto.CrearTareaRequest;
import com.pisocompartido.backendtetoca.dto.TareaResponse;
import com.pisocompartido.backendtetoca.service.AsignacionService;
import com.pisocompartido.backendtetoca.service.TareaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pisos/{pisoId}/tareas")
public class TareaController {

    private final TareaService tareaService;
    private final AsignacionService asignacionService;

    public TareaController(TareaService tareaService, AsignacionService asignacionService) {
        this.tareaService = tareaService;
        this.asignacionService = asignacionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TareaResponse crear(@PathVariable Long pisoId, @Valid @RequestBody CrearTareaRequest request) {
        TareaResponse tarea = tareaService.crear(pisoId, request);
        return tarea;
    }

    @GetMapping
    public List<TareaResponse> listar(@PathVariable Long pisoId) {
        return tareaService.listar(pisoId);
    }

    @DeleteMapping("/{tareaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long pisoId, @PathVariable Long tareaId) {
        tareaService.eliminar(pisoId, tareaId);
    }
}
