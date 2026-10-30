package com.pisocompartido.backendtetoca.repository;

import com.pisocompartido.backendtetoca.model.Asignacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {

    @Query("""
            SELECT a FROM Asignacion a
            JOIN FETCH a.tarea t
            JOIN FETCH a.conviviente c
            WHERE t.piso.id = :pisoId
            ORDER BY a.fechaLimite
            """)
    List<Asignacion> buscarPorPiso(@Param("pisoId") Long pisoId);

    @Query("""
            SELECT a FROM Asignacion a
            JOIN FETCH a.tarea t
            JOIN FETCH a.conviviente c
            WHERE t.piso.id = :pisoId
              AND a.completada = false
              AND a.fechaLimite >= :hoy
            ORDER BY a.fechaLimite
            """)
    List<Asignacion> buscarPendientes(@Param("pisoId") Long pisoId, @Param("hoy") LocalDate hoy);

    @Query("""
            SELECT a FROM Asignacion a
            JOIN FETCH a.tarea t
            JOIN FETCH a.conviviente c
            WHERE t.piso.id = :pisoId
              AND a.completada = false
              AND a.fechaLimite < :hoy
            ORDER BY a.fechaLimite
            """)
    List<Asignacion> buscarVencidas(@Param("pisoId") Long pisoId, @Param("hoy") LocalDate hoy);

    @Query("""
            SELECT a FROM Asignacion a
            JOIN FETCH a.tarea t
            JOIN FETCH a.conviviente c
            WHERE t.piso.id = :pisoId
              AND a.completada = true
            ORDER BY a.fechaCompletada DESC
            """)
    List<Asignacion> buscarCompletadas(@Param("pisoId") Long pisoId);
}