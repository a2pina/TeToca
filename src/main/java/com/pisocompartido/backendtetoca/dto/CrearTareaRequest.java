package com.pisocompartido.backendtetoca.dto;

import jakarta.validation.constraints.*;

public record CrearTareaRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
        String nombre,

        @NotNull(message = "La dificultad es obligatoria")
        @Min(value = 1, message = "La dificultad mínima es 1")
        @Max(value = 3, message = "La dificultad máxima es 3")
        Integer dificultad,

        @NotNull(message = "La frecuencia semanal es obligatoria")
        @Min(value = 1, message = "Mínimo 1 vez por semana")
        @Max(value = 21, message = "Máximo 21 veces por semana")
        Integer frecuenciaSemanal
) { }