package com.pisocompartido.backendtetoca.repository;

import com.pisocompartido.backendtetoca.model.EstadoSolicitud;
import com.pisocompartido.backendtetoca.model.SolicitudCambio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface SolicitudCambioRepository extends JpaRepository<SolicitudCambio, Long> {

    boolean existsByAsignacionIdAndEstado(Long asignacionId, EstadoSolicitud estado);

    @Query("""
               SELECT s FROM SolicitudCambio s
               JOIN FETCH s.asignacion a
               JOIN FETCH a.tarea
               JOIN FETCH s.solicitante
               WHERE s.destinatario.username = :username AND s.estado = :estado
               ORDER BY s.fechaSolicitud
               """)
    List<SolicitudCambio> recibidas(@Param("username") String username, @Param("estado") EstadoSolicitud estado);
}