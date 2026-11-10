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
    private int dificultad;

    @Column(name = "frecuencia_semanal", nullable = false)
    private int frecuenciaSemanal;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "piso_id")
    private Piso piso;

    protected Tarea() { }

    public Tarea(String nombre, int dificultad, int frecuenciaSemanal, Piso piso) {
        this.nombre = nombre;
        this.dificultad = dificultad;
        this.frecuenciaSemanal = frecuenciaSemanal;
        this.piso = piso;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public int getDificultad() { return dificultad; }
    public int getFrecuenciaSemanal() { return frecuenciaSemanal; }
    public Piso getPiso() { return piso; }
}