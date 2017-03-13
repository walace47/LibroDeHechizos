package com.joan.librohechizos.modelo;

/**
 * Created by Joan on 08/03/2017.
 */

public class HechizoAprendido {
    private String idPersonaje;
    private String idHechizo;
    private boolean preparado;

    public HechizoAprendido(String idPersonaje, String idHechizo, boolean preparado) {
        this.idPersonaje = idPersonaje;
        this.idHechizo = idHechizo;
        this.preparado = preparado;
    }

    public String getIdPersonaje() {
        return idPersonaje;
    }

    public String getIdHechizo() {
        return idHechizo;
    }

    public boolean isPreparado() {
        return preparado;
    }

    public void setIdPersonaje(String idPersonaje) {
        this.idPersonaje = idPersonaje;
    }

    public void setIdHechizo(String idHechizo) {
        this.idHechizo = idHechizo;
    }

    public void setPreparado(boolean preparado) {
        this.preparado = preparado;
    }
}
