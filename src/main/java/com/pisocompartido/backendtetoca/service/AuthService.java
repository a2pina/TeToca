package com.pisocompartido.backendtetoca.service;

import com.pisocompartido.backendtetoca.dto.ConvivienteResponse;
import com.pisocompartido.backendtetoca.dto.RegistroRequest;
import com.pisocompartido.backendtetoca.dto.UsuarioResponse;
import com.pisocompartido.backendtetoca.model.Conviviente;
import com.pisocompartido.backendtetoca.model.Piso;
import com.pisocompartido.backendtetoca.repository.ConvivienteRepository;
import com.pisocompartido.backendtetoca.repository.PisoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final PisoRepository pisoRepository;
    private final ConvivienteRepository convivienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final TareaService tareaService;


    public AuthService(PisoRepository pisoRepository, ConvivienteRepository convivienteRepository,
                       PasswordEncoder passwordEncoder, TareaService tareaService) {
        this.pisoRepository = pisoRepository;
        this.convivienteRepository = convivienteRepository;
        this.passwordEncoder = passwordEncoder;
        this.tareaService = tareaService;

    }

    @Transactional
    public ConvivienteResponse registrar(RegistroRequest request) {
        if (convivienteRepository.existsByUsername(request.username())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ese nombre de usuario ya está en uso");
        }
        if (convivienteRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ese email ya está registrado");
        }

        Piso piso;
        if (request.nombrePiso() != null && !request.nombrePiso().isBlank()) {
            piso = pisoRepository.save(new Piso(request.nombrePiso().trim()));
            tareaService.crearTareasBasicas(piso);
        } else if (request.codigoPiso() != null && !request.codigoPiso().isBlank()) {
            piso = pisoRepository.findByCodigo(request.codigoPiso().trim().toUpperCase())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "No existe ningún piso con ese código"));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Indica el código de un piso o el nombre de uno nuevo");
        }

        Conviviente conviviente = convivienteRepository.save(new Conviviente(
                request.nombre(), request.email(), piso,
                request.username(), passwordEncoder.encode(request.password())));
        return ConvivienteResponse.from(conviviente);
    }

    @Transactional(readOnly = true)
    public UsuarioResponse usuarioActual(String username) {
        return convivienteRepository.findByUsername(username)
                .map(UsuarioResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }
}