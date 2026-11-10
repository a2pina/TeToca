package com.pisocompartido.backendtetoca.controller;

import com.pisocompartido.backendtetoca.dto.AsignacionResponse;
import com.pisocompartido.backendtetoca.dto.CrearAsignacionRequest;
import com.pisocompartido.backendtetoca.model.EstadoAsignacion;
import com.pisocompartido.backendtetoca.service.AsignacionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public AsignacionResponse completar(@PathVariable Long id,
                                        org.springframework.security.core.Authentication auth) {
        boolean esAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return asignacionService.completar(id, auth.getName(), esAdmin);
    }
    @PreAuthorize("@acceso.puedeVerPiso(#pisoId, authentication)")
    @GetMapping("/pisos/{pisoId}/asignaciones")
    public List<AsignacionResponse> listar(@PathVariable Long pisoId,
                                           @RequestParam(required = false) EstadoAsignacion estado) {
        return asignacionService.listar(pisoId, estado);
    }

    @PostMapping("/pisos/{pisoId}/reparto")
    @ResponseStatus(HttpStatus.CREATED)
    @org.springframework.security.access.prepost.PreAuthorize("@acceso.puedeVerPiso(#pisoId, authentication)")
    public List<AsignacionResponse> repartir(@PathVariable Long pisoId) {
        return asignacionService.repartirSemana(pisoId);
    }
    @PatchMapping("/asignaciones/{id}/conviviente")
    public AsignacionResponse reasignar(@PathVariable Long id,
                                        @Valid @RequestBody com.pisocompartido.backendtetoca.dto.ReasignarRequest request,
                                        org.springframework.security.core.Authentication auth) {
        boolean esAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return asignacionService.reasignar(id, request.convivienteId(), auth.getName(), esAdmin);
    }
}