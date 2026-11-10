package com.pisocompartido.backendtetoca.dto;

import com.pisocompartido.backendtetoca.model.SolicitudCambio;
import java.time.LocalDate;

public record SolicitudResponse(Long id, Long asignacionId, String tarea, int dificultad,
                                String solicitante, String destinatario, LocalDate fechaLimite, String estado) {

    public static SolicitudResponse from(SolicitudCambio s) {
        return new SolicitudResponse(s.getId(), s.getAsignacion().getId(),
                s.getAsignacion().getTarea().getNombre(), s.getAsignacion().getTarea().getDificultad(),
                s.getSolicitante().getNombre(), s.getDestinatario().getNombre(),
                s.getAsignacion().getFechaLimite(), s.getEstado().name());
    }
}