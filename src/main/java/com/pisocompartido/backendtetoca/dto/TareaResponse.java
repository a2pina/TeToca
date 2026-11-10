package com.pisocompartido.backendtetoca.dto;

import com.pisocompartido.backendtetoca.model.Tarea;

public record TareaResponse(Long id, String nombre, int dificultad, int frecuenciaSemanal) {

    public static TareaResponse from(Tarea tarea) {
        return new TareaResponse(tarea.getId(), tarea.getNombre(),
                tarea.getDificultad(), tarea.getFrecuenciaSemanal());
    }
}