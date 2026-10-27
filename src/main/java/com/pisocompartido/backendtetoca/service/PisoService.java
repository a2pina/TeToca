package com.pisocompartido.backendtetoca.service;

import com.pisocompartido.backendtetoca.dto.CrearPisoRequest;
import com.pisocompartido.backendtetoca.dto.PisoResponse;
import com.pisocompartido.backendtetoca.model.Piso;
import com.pisocompartido.backendtetoca.repository.PisoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PisoService {

    private final PisoRepository pisoRepository;

    public PisoService(PisoRepository pisoRepository) {
        this.pisoRepository = pisoRepository;
    }

    @Transactional
    public PisoResponse crear(CrearPisoRequest request) {
        Piso piso = pisoRepository.save(new Piso(request.nombre()));
        return PisoResponse.from(piso);
    }

    @Transactional(readOnly = true)
    public PisoResponse obtener(Long id) {
        return PisoResponse.from(buscarEntidad(id));
    }

    public Piso buscarEntidad(Long id) {
        return pisoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe el piso " + id));
    }
}
