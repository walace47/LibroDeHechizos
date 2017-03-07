package com.joan.librohechizos.modelo;

/**
 * Created by Giuliano on 07/03/2017.
 */

public class Raza {

    private String idRaza;
    private String nombre;

    public Raza(String idRaza, String nombre) {
        this.idRaza = idRaza;
        this.nombre = nombre;
    }

    public String getIdRaza() {
        return idRaza;
    }

    public void setIdRaza(String idRaza) {
        this.idRaza = idRaza;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
