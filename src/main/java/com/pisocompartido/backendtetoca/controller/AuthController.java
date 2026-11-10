package com.pisocompartido.backendtetoca.controller;

import com.pisocompartido.backendtetoca.dto.ConvivienteResponse;
import com.pisocompartido.backendtetoca.dto.RegistroRequest;
import com.pisocompartido.backendtetoca.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    public ConvivienteResponse registrar(@Valid @RequestBody RegistroRequest request) {
        return authService.registrar(request);
    }
}