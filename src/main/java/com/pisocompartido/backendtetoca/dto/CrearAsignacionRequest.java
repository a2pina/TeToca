package com.pisocompartido.backendtetoca.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CrearAsignacionRequest(
        @NotNull(message = "El conviviente es obligatorio")
        Long convivienteId,

        @NotNull(message = "La fecha límite es obligatoria")
        LocalDate fechaLimite
) { }