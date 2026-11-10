package com.pisocompartido.backendtetoca.dto;

import jakarta.validation.constraints.NotNull;

public record SolicitudRequest(@NotNull(message = "Indica a qué compañero se lo pides") Long convivienteId) { }