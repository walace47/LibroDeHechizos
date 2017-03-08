package com.joan.librohechizos.modelo;

/**
 * Created by Joan on 08/03/2017.
 */

public class HechizoPorClase {
    private String idClase;
    private String idHechizo;

    public HechizoPorClase(String idClase, String idHechizo) {
        this.idClase = idClase;
        this.idHechizo = idHechizo;
    }

    public String getIdClase() {
        return idClase;
    }

    public String getIdHechizo() {
        return idHechizo;
    }

    public void setIdClase(String idClase) {
        this.idClase = idClase;
    }

    public void setIdHechizo(String idHechizo) {
        this.idHechizo = idHechizo;
    }
}
