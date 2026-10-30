package com.pisocompartido.backendtetoca.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "asignacion")
public class Asignacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tarea_id")
    private Tarea tarea;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conviviente_id")
    private Conviviente conviviente;

    @Column(name = "fecha_limite", nullable = false)
    private LocalDate fechaLimite;

    @Column(nullable = false)
    private boolean completada;

    @Column(name = "fecha_completada")
    private LocalDateTime fechaCompletada;

    protected Asignacion() { }

    public Asignacion(Tarea tarea, Conviviente conviviente, LocalDate fechaLimite) {
        this.tarea = tarea;
        this.conviviente = conviviente;
        this.fechaLimite = fechaLimite;
        this.completada = false;
    }

    public void completar() {
        this.completada = true;
        this.fechaCompletada = LocalDateTime.now();
    }

    public EstadoAsignacion getEstado() {
        if (completada) {
            return EstadoAsignacion.COMPLETADA;
        }
        if (fechaLimite.isBefore(LocalDate.now())) {
            return EstadoAsignacion.VENCIDA;
        }
        return EstadoAsignacion.PENDIENTE;
    }

    public Long getId() { return id; }
    public Tarea getTarea() { return tarea; }
    public Conviviente getConviviente() { return conviviente; }
    public LocalDate getFechaLimite() { return fechaLimite; }
    public boolean isCompletada() { return completada; }
    public LocalDateTime getFechaCompletada() { return fechaCompletada; }
}