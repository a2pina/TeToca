package com.pisocompartido.backendtetoca.service;

import com.pisocompartido.backendtetoca.dto.CrearTareaRequest;
import com.pisocompartido.backendtetoca.dto.TareaResponse;
import com.pisocompartido.backendtetoca.model.Piso;
import com.pisocompartido.backendtetoca.model.Tarea;
import com.pisocompartido.backendtetoca.repository.AsignacionRepository;
import com.pisocompartido.backendtetoca.repository.TareaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class TareaService {

    private final TareaRepository tareaRepository;
    private final PisoService pisoService;
    private final AsignacionRepository asignacionRepository;

    public TareaService(TareaRepository tareaRepository, PisoService pisoService, AsignacionRepository asignacionRepository) {
        this.tareaRepository = tareaRepository;
        this.pisoService = pisoService;
        this.asignacionRepository = asignacionRepository;
    }

    @Transactional
    public TareaResponse crear(Long pisoId, CrearTareaRequest request) {
        Piso piso = pisoService.buscarEntidad(pisoId);
        Tarea tarea = tareaRepository.save(
                new Tarea(request.nombre(), request.dificultad(), request.frecuenciaSemanal(), piso));
        return TareaResponse.from(tarea);
    }

    @Transactional(readOnly = true)
    public List<TareaResponse> listar(Long pisoId) {
        pisoService.buscarEntidad(pisoId);
        return tareaRepository.findByPisoIdOrderByNombreAsc(pisoId).stream()
                .map(TareaResponse::from)
                .toList();
    }
    @Transactional
    public void crearTareasBasicas(Piso piso) {
        tareaRepository.saveAll(List.of(
                new Tarea("Fregar los platos", 1, 7, piso),
                new Tarea("Sacar la basura", 1, 7, piso),
                new Tarea("Vaciar el lavavajillas", 1, 7, piso),
                new Tarea("Poner el lavavajillas", 2, 7, piso),
                new Tarea("Limpiar el polvo", 2, 1, piso),
                new Tarea("Regar las plantas", 2, 1, piso),
                new Tarea("Cambiar las sábanas", 2, 1, piso),
                new Tarea("Doblar la ropa", 2, 2, piso),
                new Tarea("Limpiar el baño", 3, 2, piso),
                new Tarea("Barrer y fregar zonas comunes", 3, 2, piso),
                new Tarea("Poner y tender la lavadora", 3, 2, piso)));
    }

    public Tarea buscarEntidad(Long id) {
        return tareaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe la tarea " + id));
    }
    @Transactional
    public void eliminar(Long pisoId, Long tareaId) {
        Tarea tarea = buscarEntidad(tareaId);
        if (!tarea.getPiso().getId().equals(pisoId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La tarea no pertenece a este piso");
        }
        asignacionRepository.deleteByTarea_Id(tareaId);
        tareaRepository.delete(tarea);
    }
}