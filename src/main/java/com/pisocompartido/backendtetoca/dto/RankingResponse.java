package com.pisocompartido.backendtetoca.dto;

import com.pisocompartido.backendtetoca.repository.PuntosConviviente;

public record RankingResponse(int posicion, Long convivienteId, String nombre, long puntos) {

    public static RankingResponse from(int posicion, PuntosConviviente p) {
        return new RankingResponse(posicion, p.getConvivienteId(), p.getNombre(), p.getPuntos());
    }
}