package com.pisocompartido.backendtetoca.service;

import com.pisocompartido.backendtetoca.dto.CrearTareaRequest;
import com.pisocompartido.backendtetoca.dto.TareaResponse;
import com.pisocompartido.backendtetoca.model.Piso;
import com.pisocompartido.backendtetoca.model.Tarea;
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

    public TareaService(TareaRepository tareaRepository, PisoService pisoService) {
        this.tareaRepository = tareaRepository;
        this.pisoService = pisoService;
    }

    @Transactional
    public TareaResponse crear(Long pisoId, CrearTareaRequest request) {
        Piso piso = pisoService.buscarEntidad(pisoId);
        Tarea tarea = tareaRepository.save(new Tarea(request.nombre(), request.puntos(), piso));
        return TareaResponse.from(tarea);
    }

    @Transactional(readOnly = true)
    public List<TareaResponse> listar(Long pisoId) {
        pisoService.buscarEntidad(pisoId);
        return tareaRepository.findByPisoIdOrderByNombreAsc(pisoId).stream()
                .map(TareaResponse::from)
                .toList();
    }

    public Tarea buscarEntidad(Long id) {
        return tareaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe la tarea " + id));
    }
}