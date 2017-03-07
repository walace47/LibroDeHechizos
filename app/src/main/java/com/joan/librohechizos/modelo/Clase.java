package com.joan.librohechizos.modelo;

/**
 * Created by Giuliano on 07/03/2017.
 */

public class Clase {

    private String idClase;
    private String nombre;

    public Clase(String idClase, String nombre) {
        this.idClase = idClase;
        this.nombre = nombre;
    }

    public String getIdClase() {
        return idClase;
    }

    public void setIdClase(String idClase) {
        this.idClase = idClase;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
