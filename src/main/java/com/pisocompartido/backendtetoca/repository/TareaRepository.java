package com.pisocompartido.backendtetoca.repository;

import com.pisocompartido.backendtetoca.model.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TareaRepository extends JpaRepository<Tarea, Long> {

    List<Tarea> findByPisoIdOrderByNombreAsc(Long pisoId);
}