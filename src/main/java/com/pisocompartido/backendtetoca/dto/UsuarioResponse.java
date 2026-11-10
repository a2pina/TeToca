package com.pisocompartido.backendtetoca.dto;

import com.pisocompartido.backendtetoca.model.Conviviente;

public record UsuarioResponse(Long id, String nombre, String username, String rol,
                              Long pisoId, String pisoNombre) {

    public static UsuarioResponse from(Conviviente c) {
        return new UsuarioResponse(c.getId(), c.getNombre(), c.getUsername(), c.getRol().name(),
                c.getPiso() != null ? c.getPiso().getId() : null,
                c.getPiso() != null ? c.getPiso().getNombre() : null);
    }
}
