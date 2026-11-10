package com.pisocompartido.backendtetoca.controller;

import com.pisocompartido.backendtetoca.dto.SolicitudRequest;
import com.pisocompartido.backendtetoca.dto.SolicitudResponse;
import com.pisocompartido.backendtetoca.service.SolicitudService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SolicitudController {

    private final SolicitudService solicitudService;

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @PostMapping("/asignaciones/{id}/solicitudes")
    @ResponseStatus(HttpStatus.CREATED)
    public SolicitudResponse crear(@PathVariable Long id, @Valid @RequestBody SolicitudRequest request,
                                   Principal principal) {
        return solicitudService.crear(id, request.convivienteId(), principal.getName());
    }

    @GetMapping("/solicitudes/recibidas")
    public List<SolicitudResponse> recibidas(Principal principal) {
        return solicitudService.recibidas(principal.getName());
    }

    @PatchMapping("/solicitudes/{id}/aceptar")
    public SolicitudResponse aceptar(@PathVariable Long id, Principal principal) {
        return solicitudService.responder(id, principal.getName(), true);
    }

    @PatchMapping("/solicitudes/{id}/rechazar")
    public SolicitudResponse rechazar(@PathVariable Long id, Principal principal) {
        return solicitudService.responder(id, principal.getName(), false);
    }
}