package com.pisocompartido.backendtetoca.service;

import com.pisocompartido.backendtetoca.dto.AsignacionResponse;
import com.pisocompartido.backendtetoca.dto.CrearAsignacionRequest;
import com.pisocompartido.backendtetoca.model.*;
import com.pisocompartido.backendtetoca.repository.AsignacionRepository;
import com.pisocompartido.backendtetoca.repository.ConvivienteRepository;
import com.pisocompartido.backendtetoca.repository.TareaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
public class AsignacionService {

    private final AsignacionRepository asignacionRepository;
    private final TareaService tareaService;
    private final ConvivienteService convivienteService;
    private final PisoService pisoService;
    private final TareaRepository tareaRepository;
    private final ConvivienteRepository convivienteRepository;

    public AsignacionService(AsignacionRepository asignacionRepository, TareaService tareaService,
                             ConvivienteService convivienteService, PisoService pisoService,
                             TareaRepository tareaRepository, ConvivienteRepository convivienteRepository) {
        this.asignacionRepository = asignacionRepository;
        this.tareaService = tareaService;
        this.convivienteService = convivienteService;
        this.pisoService = pisoService;
        this.tareaRepository = tareaRepository;
        this.convivienteRepository = convivienteRepository;
    }

    @Transactional
    public AsignacionResponse asignar(Long tareaId, CrearAsignacionRequest request) {
        Tarea tarea = tareaService.buscarEntidad(tareaId);
        Conviviente conviviente = convivienteService.buscarEntidad(request.convivienteId());
        if (conviviente.getPiso() == null || !tarea.getPiso().getId().equals(conviviente.getPiso().getId())) {
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

    @Transactional
    public List<AsignacionResponse> repartirSemana(Long pisoId) {
        pisoService.buscarEntidad(pisoId);
        LocalDate domingo = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        List<Conviviente> convivientes = new ArrayList<>(convivienteRepository.findByPisoId(pisoId));
        if (convivientes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El piso no tiene convivientes");
        }
        Collections.shuffle(convivientes);

        Map<Long, Integer> carga = new HashMap<>();
        convivientes.forEach(c -> carga.put(c.getId(), 0));

        // Lo ya hecho esta semana se respeta y cuenta como carga; lo pendiente se vuelve a repartir
        List<Asignacion> estaSemana = asignacionRepository.findByTarea_Piso_IdAndFechaLimite(pisoId, domingo);
        Set<Long> tareasHechas = new HashSet<>();
        for (Asignacion a : estaSemana) {
            if (a.isCompletada()) {
                tareasHechas.add(a.getTarea().getId());
                carga.merge(a.getConviviente().getId(), a.getTarea().getDificultad(), Integer::sum);
            }
        }
        asignacionRepository.deleteAll(estaSemana.stream().filter(a -> !a.isCompletada()).toList());

        List<Tarea> tareas = new ArrayList<>(tareaRepository.findByPisoIdOrderByNombreAsc(pisoId));
        tareas.removeIf(t -> tareasHechas.contains(t.getId()));
        Collections.shuffle(tareas);
        tareas.sort(Comparator.comparingInt(Tarea::getDificultad).reversed());

        List<Asignacion> nuevas = new ArrayList<>();
        for (Tarea tarea : tareas) {
            Conviviente elegido = convivientes.stream()
                    .min(Comparator.comparingInt(c -> carga.getOrDefault(c.getId(), 0)))
                    .orElseThrow();
            carga.merge(elegido.getId(), tarea.getDificultad(), Integer::sum);
            nuevas.add(new Asignacion(tarea, elegido, domingo));
        }
        return asignacionRepository.saveAll(nuevas).stream().map(AsignacionResponse::from).toList();
    }

}