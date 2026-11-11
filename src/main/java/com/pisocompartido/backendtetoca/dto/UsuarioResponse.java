package com.pisocompartido.backendtetoca.dto;

import com.pisocompartido.backendtetoca.model.Conviviente;

public record UsuarioResponse(Long id, String nombre, String username, String rol,
                              Long pisoId, String pisoNombre, String pisoCodigo, String avatar) {

    public static UsuarioResponse from(Conviviente c) {
        boolean tienePiso = c.getPiso() != null;
        return new UsuarioResponse(c.getId(), c.getNombre(), c.getUsername(), c.getRol().name(),
                tienePiso ? c.getPiso().getId() : null,
                tienePiso ? c.getPiso().getNombre() : null,
                tienePiso ? c.getPiso().getCodigo() : null,
                c.getAvatar()
        );
    }
}