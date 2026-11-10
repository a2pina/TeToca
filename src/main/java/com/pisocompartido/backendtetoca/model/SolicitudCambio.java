package com.pisocompartido.backendtetoca.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "solicitud_cambio")
public class SolicitudCambio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "asignacion_id")
    private Asignacion asignacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "solicitante_id")
    private Conviviente solicitante;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "destinatario_id")
    private Conviviente destinatario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoSolicitud estado = EstadoSolicitud.PENDIENTE;

    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDateTime fechaSolicitud;

    protected SolicitudCambio() { }

    public SolicitudCambio(Asignacion asignacion, Conviviente solicitante, Conviviente destinatario) {
        this.asignacion = asignacion;
        this.solicitante = solicitante;
        this.destinatario = destinatario;
        this.fechaSolicitud = LocalDateTime.now();
    }

    public void aceptar() {
        this.estado = EstadoSolicitud.ACEPTADA;
        this.asignacion.reasignar(destinatario);
    }

    public void rechazar() {
        this.estado = EstadoSolicitud.RECHAZADA;
    }

    public Long getId() { return id; }
    public Asignacion getAsignacion() { return asignacion; }
    public Conviviente getSolicitante() { return solicitante; }
    public Conviviente getDestinatario() { return destinatario; }
    public EstadoSolicitud getEstado() { return estado; }
    public LocalDateTime getFechaSolicitud() { return fechaSolicitud; }
}