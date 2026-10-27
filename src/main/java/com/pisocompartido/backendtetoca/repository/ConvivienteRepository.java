package com.pisocompartido.backendtetoca.repository;

import com.pisocompartido.backendtetoca.model.Conviviente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ConvivienteRepository extends JpaRepository<Conviviente, Long> {

    List<Conviviente> findByPisoId(Long pisoId);

    boolean existsByEmail(String email);
}