package com.proyectofinal.saul.comunioadmin;

/**
 * Created by Saul on 25/05/2016.
 */
public class Usuario {
    private String nombre;
    private String puntos;
    private String numEstrellas;
    private String beneficio;

    public Usuario() {}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPuntos() {
        return puntos;
    }

    public void setPuntos(String puntos) {
        this.puntos = puntos;
    }

    public String getNumEstrellas() {
        return numEstrellas;
    }

    public void setNumEstrellas(String numEstrellas) {
        this.numEstrellas = numEstrellas;
    }

    public String getBeneficio() {
        return beneficio;
    }

    public void setBeneficio(String beneficio) {
        this.beneficio = beneficio;
    }
}
