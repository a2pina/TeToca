package com.pisocompartido.backendtetoca.service;

import com.pisocompartido.backendtetoca.dto.SolicitudResponse;
import com.pisocompartido.backendtetoca.model.*;
import com.pisocompartido.backendtetoca.repository.AsignacionRepository;
import com.pisocompartido.backendtetoca.repository.ConvivienteRepository;
import com.pisocompartido.backendtetoca.repository.SolicitudCambioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class SolicitudService {

    private final SolicitudCambioRepository solicitudRepository;
    private final AsignacionRepository asignacionRepository;
    private final ConvivienteRepository convivienteRepository;

    public SolicitudService(SolicitudCambioRepository solicitudRepository,
                            AsignacionRepository asignacionRepository,
                            ConvivienteRepository convivienteRepository) {
        this.solicitudRepository = solicitudRepository;
        this.asignacionRepository = asignacionRepository;
        this.convivienteRepository = convivienteRepository;
    }

    @Transactional
    public SolicitudResponse crear(Long asignacionId, Long destinatarioId, String username) {
        Asignacion asignacion = asignacionRepository.findById(asignacionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe la asignación"));
        Conviviente yo = convivienteRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario no válido"));

        if (!asignacion.getConviviente().getId().equals(yo.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo puedes pedir cambios de tus propias tareas");
        }
        if (asignacion.isCompletada()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La tarea ya está completada");
        }

        Conviviente destinatario = convivienteRepository.findById(destinatarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe ese compañero"));
        if (destinatario.getId().equals(yo.getId())
                || destinatario.getPiso() == null
                || !destinatario.getPiso().getId().equals(yo.getPiso().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Elige a un compañero de tu piso");
        }
        if (solicitudRepository.existsByAsignacionIdAndEstado(asignacionId, EstadoSolicitud.PENDIENTE)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya hay una solicitud pendiente para esta tarea");
        }

        return SolicitudResponse.from(solicitudRepository.save(new SolicitudCambio(asignacion, yo, destinatario)));
    }

    @Transactional(readOnly = true)
    public List<SolicitudResponse> recibidas(String username) {
        return solicitudRepository.recibidas(username, EstadoSolicitud.PENDIENTE).stream()
                .map(SolicitudResponse::from)
                .toList();
    }

    @Transactional
    public SolicitudResponse responder(Long id, String username, boolean acepta) {
        SolicitudCambio solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe la solicitud"));
        if (!username.equals(solicitud.getDestinatario().getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Esta solicitud no es para ti");
        }
        if (solicitud.getEstado() != EstadoSolicitud.PENDIENTE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La solicitud ya fue respondida");
        }
        if (solicitud.getAsignacion().isCompletada()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La tarea ya está completada");
        }
        if (acepta) {
            solicitud.aceptar();
        } else {
            solicitud.rechazar();
        }
        return SolicitudResponse.from(solicitud);
    }
}