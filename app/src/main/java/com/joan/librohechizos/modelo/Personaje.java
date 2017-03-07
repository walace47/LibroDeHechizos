package com.joan.librohechizos.modelo;

/**
 * Created by Giuliano on 06/03/2017.
 */

public class Personaje {

    private String idPersonaje;
    private String nombre;
    private String idClase;
    private String idRaza;

    public Personaje(String idPersonaje, String nombre, String idClase, String idRaza) {
        this.idPersonaje = idPersonaje;
        this.nombre = nombre;
        this.idClase = idClase;
        this.idRaza = idRaza;
    }

    public String getIdPersonaje() {
        return idPersonaje;
    }

    public void setIdPersonaje(String idPersonaje) {
        this.idPersonaje = idPersonaje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdClase() {
        return idClase;
    }

    public void setIdClase(String idClase) {
        this.idClase = idClase;
    }

    public String getIdRaza() {
        return idRaza;
    }

    public void setIdRaza(String idRaza) {
        this.idRaza = idRaza;
    }
}
