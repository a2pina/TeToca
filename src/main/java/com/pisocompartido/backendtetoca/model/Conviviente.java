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

    @Column(length = 16)
    private String avatar;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "piso_id")
    private Piso piso;

    @Column(unique = true, length = 50)
    private String username;

    @Column(length = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Rol rol = Rol.CONVIVIENTE;

    protected Conviviente() { }

    public Conviviente(String nombre, String email, Piso piso) {
        this.nombre = nombre;
        this.email = email;
        this.piso = piso;
    }

    public Conviviente(String nombre, String email, Piso piso, String username, String password) {
        this(nombre, email, piso);
        this.username = username;
        this.password = password;
    }
    public void actualizarPerfil(String nombre, String avatar) {
        this.nombre = nombre;
        this.avatar = avatar;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public Piso getPiso() { return piso; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Rol getRol() { return rol; }
    public String getAvatar() { return avatar; }

}
