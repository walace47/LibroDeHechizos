package com.joan.librohechizos.modelo;

/**
 * Created by Joan on 08/03/2017.
 */

public class Hechizo {
    private String idHechizo;
    private String nombre;
    private int rango;
    private String escuela;
    private String tiempoDeCasteo;
    private String duracion;
    private boolean concentracion;
    private boolean ritual;
    private boolean componenteVerbal;
    private boolean componenteSomatico;
    private String componenteMaterial;
    private String descripcion;
    private String aMayorNivel;

    public Hechizo(String idHechizo,String nombre, int rango, String escuela, String tiempoDeCasteo, String duracion, boolean concentracion, boolean ritual, boolean componenteVerbal, boolean componenteSomatico, String componenteMaterial, String descripcion, String aMayorNivel) {
        this.idHechizo = idHechizo;
        this.nombre=nombre;
        this.rango = rango;
        this.escuela = escuela;
        this.tiempoDeCasteo = tiempoDeCasteo;
        this.duracion = duracion;
        this.concentracion = concentracion;
        this.ritual = ritual;
        this.componenteVerbal = componenteVerbal;
        this.componenteSomatico = componenteSomatico;
        this.componenteMaterial = componenteMaterial;
        this.descripcion = descripcion;
        this.aMayorNivel = aMayorNivel;
    }

    public String getIdHechizo() {
        return idHechizo;
    }

    public int getRango() {
        return rango;
    }

    public String getEscuela() {
        return escuela;
    }

    public String getTiempoDeCasteo() {
        return tiempoDeCasteo;
    }

    public String getDuracion() {
        return duracion;
    }

    public boolean isConcentracion() {
        return concentracion;
    }

    public boolean isRitual() {
        return ritual;
    }

    public boolean isComponenteVerbal() {
        return componenteVerbal;
    }

    public boolean isComponenteSomatico() {
        return componenteSomatico;
    }

    public String getComponenteMaterial() {
        return componenteMaterial;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getaMayorNivel() {
        return aMayorNivel;
    }

    public void setIdHechizo(String idHechizo) {
        this.idHechizo = idHechizo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setRango(int rango) {
        this.rango = rango;
    }

    public void setEscuela(String escuela) {
        this.escuela = escuela;
    }

    public void setTiempoDeCasteo(String tiempoDeCasteo) {
        this.tiempoDeCasteo = tiempoDeCasteo;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public void setConcentracion(boolean concentracion) {
        this.concentracion = concentracion;
    }

    public void setRitual(boolean ritual) {
        this.ritual = ritual;
    }

    public void setComponenteVerbal(boolean componenteVerbal) {
        this.componenteVerbal = componenteVerbal;
    }

    public void setComponenteSomatico(boolean componenteSomatico) {
        this.componenteSomatico = componenteSomatico;
    }

    public void setComponenteMaterial(String compoeneteMaterial) {
        this.componenteMaterial = compoeneteMaterial;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setaMayorNivel(String aMayorNivel) {
        this.aMayorNivel = aMayorNivel;
    }
}
