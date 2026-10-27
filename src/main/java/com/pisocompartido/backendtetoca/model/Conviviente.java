package com.pisocompartido.backendtetoca.model;

import jakarta.persistence.*;

@Entity
@Table(name = "conviviente")
public class Conviviente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "piso_id")
    private Piso piso;

    protected Conviviente() { }

    public Conviviente(String nombre, String email, Piso piso) {
        this.nombre = nombre;
        this.email = email;
        this.piso = piso;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public Piso getPiso() { return piso; }
}