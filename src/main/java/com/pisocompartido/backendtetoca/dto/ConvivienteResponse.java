package com.pisocompartido.backendtetoca.dto;

import com.pisocompartido.backendtetoca.model.Conviviente;

public record ConvivienteResponse(Long id, String nombre, String email) {

    public static ConvivienteResponse from(Conviviente conviviente) {
        return new ConvivienteResponse(
                conviviente.getId(), conviviente.getNombre(), conviviente.getEmail());
    }
}