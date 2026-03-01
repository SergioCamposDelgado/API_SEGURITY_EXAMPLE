package com.dwes.security.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "ciudades")
public class Ciudad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Double latitud;
    private Double longitud;

    // Constructores
    public Ciudad() {}
    public Ciudad(String nombre, Double latitud, Double longitud) {
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }
    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }
}