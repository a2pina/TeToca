package com.pisocompartido.backendtetoca.service;

import com.pisocompartido.backendtetoca.dto.AsignacionResponse;
import com.pisocompartido.backendtetoca.dto.CrearAsignacionRequest;
import com.pisocompartido.backendtetoca.model.Asignacion;
import com.pisocompartido.backendtetoca.model.Conviviente;
import com.pisocompartido.backendtetoca.model.EstadoAsignacion;
import com.pisocompartido.backendtetoca.model.Tarea;
import com.pisocompartido.backendtetoca.repository.AsignacionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.util.List;

@Service
public class AsignacionService {

    private final AsignacionRepository asignacionRepository;
    private final TareaService tareaService;
    private final ConvivienteService convivienteService;
    private final PisoService pisoService;

    public AsignacionService(AsignacionRepository asignacionRepository,
                             TareaService tareaService,
                             ConvivienteService convivienteService,
                             PisoService pisoService) {
        this.asignacionRepository = asignacionRepository;
        this.tareaService = tareaService;
        this.convivienteService = convivienteService;
        this.pisoService = pisoService;
    }

    @Transactional
    public AsignacionResponse asignar(Long tareaId, CrearAsignacionRequest request) {
        Tarea tarea = tareaService.buscarEntidad(tareaId);
        Conviviente conviviente = convivienteService.buscarEntidad(request.convivienteId());

        if (!tarea.getPiso().getId().equals(conviviente.getPiso().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El conviviente no pertenece al piso de la tarea");
        }

        Asignacion asignacion = asignacionRepository.save(
                new Asignacion(tarea, conviviente, request.fechaLimite()));
        return AsignacionResponse.from(asignacion);
    }

    @Transactional
    public AsignacionResponse completar(Long id) {
        Asignacion asignacion = asignacionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe la asignación " + id));

        if (asignacion.isCompletada()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La tarea ya estaba completada");
        }

        asignacion.completar();
        return AsignacionResponse.from(asignacion);
    }

    @Transactional(readOnly = true)
    public List<AsignacionResponse> listar(Long pisoId, EstadoAsignacion estado) {
        pisoService.buscarEntidad(pisoId);
        LocalDate hoy = LocalDate.now();

        List<Asignacion> resultado;
        if (estado == null) {
            resultado = asignacionRepository.buscarPorPiso(pisoId);
        } else {
            resultado = switch (estado) {
                case PENDIENTE -> asignacionRepository.buscarPendientes(pisoId, hoy);
                case VENCIDA -> asignacionRepository.buscarVencidas(pisoId, hoy);
                case COMPLETADA -> asignacionRepository.buscarCompletadas(pisoId);
            };
        }

        return resultado.stream().map(AsignacionResponse::from).toList();
    }
}