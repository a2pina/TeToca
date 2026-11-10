package com.pisocompartido.backendtetoca.service;

import com.pisocompartido.backendtetoca.dto.ConvivienteResponse;
import com.pisocompartido.backendtetoca.dto.CrearConvivienteRequest;
import com.pisocompartido.backendtetoca.dto.RankingResponse;
import com.pisocompartido.backendtetoca.model.Conviviente;
import com.pisocompartido.backendtetoca.model.Piso;
import com.pisocompartido.backendtetoca.repository.ConvivienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class ConvivienteService {

    private final ConvivienteRepository convivienteRepository;
    private final PisoService pisoService;

    public ConvivienteService(ConvivienteRepository convivienteRepository, PisoService pisoService) {
        this.convivienteRepository = convivienteRepository;
        this.pisoService = pisoService;
    }

    @Transactional
    public ConvivienteResponse crear(Long pisoId, CrearConvivienteRequest request) {
        Piso piso = pisoService.buscarEntidad(pisoId);

        if (convivienteRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe un conviviente con el email " + request.email());
        }

        Conviviente conviviente = convivienteRepository.save(
                new Conviviente(request.nombre(), request.email(), piso));
        return ConvivienteResponse.from(conviviente);
    }

    @Transactional(readOnly = true)
    public List<ConvivienteResponse> listar(Long pisoId) {
        pisoService.buscarEntidad(pisoId);
        return convivienteRepository.findByPisoId(pisoId).stream()
                .map(ConvivienteResponse::from)
                .toList();
    }

    public Conviviente buscarEntidad(Long id) {
        return convivienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe el conviviente " + id));
    }
    @Transactional(readOnly = true)
    public List<RankingResponse> ranking(Long pisoId) {
        pisoService.buscarEntidad(pisoId);
        java.time.LocalDateTime inicioMes = java.time.LocalDate.now().withDayOfMonth(1).atStartOfDay();
        List<com.pisocompartido.backendtetoca.repository.PuntosConviviente> filas =
                convivienteRepository.ranking(pisoId, inicioMes);
        return java.util.stream.IntStream.range(0, filas.size())
                .mapToObj(i -> RankingResponse.from(i + 1, filas.get(i)))
                .toList();
    }
}
