package com.pisocompartido.backendtetoca.dto;

import com.pisocompartido.backendtetoca.model.Asignacion;
import com.pisocompartido.backendtetoca.model.EstadoAsignacion;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record AsignacionResponse(
        Long id, Long tareaId, String tarea, int dificultad,
        Long convivienteId, String conviviente,
        LocalDate fechaLimite, EstadoAsignacion estado,
        LocalDateTime fechaAsignacion, LocalDateTime fechaCompletada,
        String avatar) {

    public static AsignacionResponse from(Asignacion a) {
        return new AsignacionResponse(
                a.getId(), a.getTarea().getId(), a.getTarea().getNombre(), a.getTarea().getDificultad(),
                a.getConviviente().getId(), a.getConviviente().getNombre(),
                a.getFechaLimite(), a.getEstado(), a.getFechaAsignacion(), a.getFechaCompletada(),
                a.getConviviente().getAvatar());
    }
}