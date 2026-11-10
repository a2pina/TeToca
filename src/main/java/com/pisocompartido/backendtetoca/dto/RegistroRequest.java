package com.pisocompartido.backendtetoca.dto;

import jakarta.validation.constraints.*;

public record RegistroRequest(
        @NotBlank(message = "El código de piso es obligatorio") String codigoPiso,
        @NotBlank(message = "El nombre es obligatorio") @Size(max = 100) String nombre,
        @NotBlank(message = "El usuario es obligatorio") @Size(min = 3, max = 50) String username,
        @NotBlank(message = "El email es obligatorio") @Email(message = "El email no es válido") String email,
        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres") String password
) { }