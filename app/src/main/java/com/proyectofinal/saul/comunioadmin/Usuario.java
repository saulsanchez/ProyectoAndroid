package com.proyectofinal.saul.comunioadmin;

/**
 * Created by Saul on 25/05/2016.
 */
public class Usuario {
    private String nombre;
    private Integer puntos;
    private Integer numEstrellas;

    public Usuario() {}

    public Usuario(String nombre, Integer puntos, Integer numEstrellas) {
        this.nombre = nombre;
        this.puntos = puntos;
        this.numEstrellas = numEstrellas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getPuntos() {
        return puntos;
    }

    public void setPuntos(Integer puntos) {
        this.puntos = puntos;
    }

    public Integer getNumEstrellas() {
        return numEstrellas;
    }

    public void setNumEstrellas(Integer numEstrellas) {
        this.numEstrellas = numEstrellas;
    }
}
