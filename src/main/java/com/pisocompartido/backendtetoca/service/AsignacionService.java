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
    public AsignacionResponse completar(Long id, String username, boolean esAdmin) {
        Asignacion asignacion = asignacionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe la asignación " + id));
        if (!esAdmin && !username.equals(asignacion.getConviviente().getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo puedes completar tus propias tareas");
        }
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
        LocalDate lunes = domingo.minusDays(6);

        List<Conviviente> convivientes = new ArrayList<>(convivienteRepository.findByPisoId(pisoId));
        if (convivientes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El piso no tiene convivientes");
        }
        Collections.shuffle(convivientes);

        Map<Long, Integer> carga = new HashMap<>();
        convivientes.forEach(c -> carga.put(c.getId(), 0));

        // Lo ya hecho esta semana se respeta y cuenta como carga; lo pendiente se vuelve a repartir
        List<Asignacion> estaSemana = asignacionRepository.findByTarea_Piso_IdAndFechaLimiteBetween(pisoId, lunes, domingo);
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

        // Las fechas límite se reparten entre hoy y el domingo
        List<LocalDate> diasRestantes = LocalDate.now().datesUntil(domingo.plusDays(1)).toList();

        List<Asignacion> nuevas = new ArrayList<>();
        for (int i = 0; i < tareas.size(); i++) {
            Tarea tarea = tareas.get(i);
            Conviviente elegido = convivientes.stream()
                    .min(Comparator.comparingInt(c -> carga.getOrDefault(c.getId(), 0)))
                    .orElseThrow();
            carga.merge(elegido.getId(), tarea.getDificultad(), Integer::sum);
            nuevas.add(new Asignacion(tarea, elegido, diasRestantes.get(i % diasRestantes.size())));
        }
        return asignacionRepository.saveAll(nuevas).stream().map(AsignacionResponse::from).toList();
    }
    @Transactional
    public void asignarSiYaHayReparto(Long tareaId) {
        Tarea tarea = tareaService.buscarEntidad(tareaId);
        Long pisoId = tarea.getPiso().getId();
        LocalDate domingo = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        List<Asignacion> estaSemana = asignacionRepository.findByTarea_Piso_IdAndFechaLimite(pisoId, domingo);
        if (estaSemana.isEmpty()) {
            return; // Aún no hay reparto: entrará en el próximo
        }

        List<Conviviente> convivientes = new ArrayList<>(convivienteRepository.findByPisoId(pisoId));
        Collections.shuffle(convivientes);
        Map<Long, Integer> carga = new HashMap<>();
        convivientes.forEach(c -> carga.put(c.getId(), 0));
        for (Asignacion a : estaSemana) {
            carga.merge(a.getConviviente().getId(), a.getTarea().getDificultad(), Integer::sum);
        }

        Conviviente elegido = convivientes.stream()
                .min(Comparator.comparingInt(c -> carga.getOrDefault(c.getId(), 0)))
                .orElseThrow();
        asignacionRepository.save(new Asignacion(tarea, elegido, domingo));
    }
    @Transactional
    public AsignacionResponse reasignar(Long id, Long convivienteId, String username, boolean esAdmin) {
        Asignacion asignacion = asignacionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe la asignación " + id));
        if (asignacion.isCompletada()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No se puede reasignar una tarea completada");
        }
        Long pisoId = asignacion.getTarea().getPiso().getId();
        if (!esAdmin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Solo el administrador puede reasignar directamente. Pide el cambio a tu compañero");
        }
        Conviviente nuevo = convivienteService.buscarEntidad(convivienteId);
        if (nuevo.getPiso() == null || !nuevo.getPiso().getId().equals(pisoId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El conviviente no pertenece a este piso");
        }
        asignacion.reasignar(nuevo);
        return AsignacionResponse.from(asignacion);
    }

}