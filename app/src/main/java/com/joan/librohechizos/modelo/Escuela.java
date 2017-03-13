package com.joan.librohechizos.modelo;

/**
 * Created by Joan on 13/03/2017.
 */

public class Escuela {
    private String nombre;
    private String idEscuela;

    public Escuela(String nombre, String idEscuela) {
        this.nombre = nombre;
        this.idEscuela = idEscuela;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdEscuela() {
        return idEscuela;
    }

    public void setIdEscuela(String idEscuela) {
        this.idEscuela = idEscuela;
    }
}

