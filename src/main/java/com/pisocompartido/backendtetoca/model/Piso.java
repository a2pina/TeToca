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
        this.codigo = generarCodigo(nombre);
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }

    public String getCodigo() { return codigo; }

    private static String generarCodigo(String nombre) {
        String limpio = java.text.Normalizer.normalize(nombre, java.text.Normalizer.Form.NFD)
                .replaceAll("[^A-Za-z]", "")
                .toUpperCase();
        String prefijo = limpio.isEmpty() ? "PISO" : limpio.substring(0, Math.min(5, limpio.length()));
        int numero = 1000 + new java.security.SecureRandom().nextInt(9000);
        return prefijo + "-" + numero;
    }
}