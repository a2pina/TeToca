package com.pisocompartido.backendtetoca.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tarea")
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false)
    private int puntos;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "piso_id")
    private Piso piso;

    protected Tarea() { }

    public Tarea(String nombre, int puntos, Piso piso) {
        this.nombre = nombre;
        this.puntos = puntos;
        this.piso = piso;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public int getPuntos() { return puntos; }
    public Piso getPiso() { return piso; }
}