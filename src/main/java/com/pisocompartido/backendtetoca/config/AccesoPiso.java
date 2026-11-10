package com.pisocompartido.backendtetoca.config;

import com.pisocompartido.backendtetoca.repository.ConvivienteRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("acceso")
public class AccesoPiso {

    private final ConvivienteRepository convivienteRepository;

    public AccesoPiso(ConvivienteRepository convivienteRepository) {
        this.convivienteRepository = convivienteRepository;
    }

    @Transactional(readOnly = true)
    public boolean puedeVerPiso(Long pisoId, Authentication auth) {
        boolean esAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (esAdmin) {
            return true;
        }
        return convivienteRepository.findByUsername(auth.getName())
                .map(c -> c.getPiso() != null && c.getPiso().getId().equals(pisoId))
                .orElse(false);
    }
}