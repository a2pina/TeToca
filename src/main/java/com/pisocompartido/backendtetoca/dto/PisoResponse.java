package com.pisocompartido.backendtetoca.dto;

import com.pisocompartido.backendtetoca.model.Piso;
import java.time.LocalDateTime;

public record PisoResponse(Long id, String nombre, LocalDateTime fechaCreacion) {

    public static PisoResponse from(Piso piso) {
        return new PisoResponse(piso.getId(), piso.getNombre(), piso.getFechaCreacion());
    }
}