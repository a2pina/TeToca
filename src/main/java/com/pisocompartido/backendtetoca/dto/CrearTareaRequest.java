
package com.pisocompartido.backendtetoca.dto;

import jakarta.validation.constraints.*;

public record CrearTareaRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
        String nombre,

        @NotNull(message = "Los puntos son obligatorios")
        @Min(value = 1, message = "Mínimo 1 punto")
        @Max(value = 5, message = "Máximo 5 puntos")
        Integer puntos
) { }