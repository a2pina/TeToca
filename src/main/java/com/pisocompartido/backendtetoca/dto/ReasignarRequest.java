package com.pisocompartido.backendtetoca.dto;

import jakarta.validation.constraints.NotNull;

public record ReasignarRequest(@NotNull(message = "El conviviente es obligatorio") Long convivienteId) { }