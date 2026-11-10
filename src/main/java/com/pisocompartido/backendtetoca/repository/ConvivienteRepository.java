package com.pisocompartido.backendtetoca.repository;

import com.pisocompartido.backendtetoca.model.Conviviente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ConvivienteRepository extends JpaRepository<Conviviente, Long> {

    List<Conviviente> findByPisoId(Long pisoId);

    boolean existsByEmail(String email);

    java.util.Optional<Conviviente> findByUsername(String username);
    boolean existsByUsername(String username);

    @org.springframework.data.jpa.repository.Query("""
           SELECT c.id AS convivienteId, c.nombre AS nombre, COALESCE(SUM(t.dificultad), 0) AS puntos
           FROM Conviviente c
           LEFT JOIN Asignacion a ON a.conviviente = c AND a.completada = true AND a.fechaCompletada >= :desde
           LEFT JOIN a.tarea t
           WHERE c.piso.id = :pisoId
           GROUP BY c.id, c.nombre
           ORDER BY puntos DESC, c.nombre ASC
           """)
    List<PuntosConviviente> ranking(
            @org.springframework.data.repository.query.Param("pisoId") Long pisoId,
            @org.springframework.data.repository.query.Param("desde") java.time.LocalDateTime desde);
}