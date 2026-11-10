package com.pisocompartido.backendtetoca.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "piso")
public class Piso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false, unique = true, length = 10)
    private String codigo;

    protected Piso() { }

    public Piso(String nombre) {
        this.nombre = nombre;
        this.fechaCreacion = LocalDateTime.now();
        this.codigo = generarCodigo();
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }

    public String getCodigo() { return codigo; }

    private static String generarCodigo() {
        String letras = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        java.security.SecureRandom aleatorio = new java.security.SecureRandom();
        StringBuilder codigo = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            codigo.append(letras.charAt(aleatorio.nextInt(letras.length())));
        }
        return codigo.toString();
    }
}