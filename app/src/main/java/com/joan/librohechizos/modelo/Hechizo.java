package com.joan.librohechizos.modelo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Joan on 08/03/2017.
 */

public class Hechizo  {
    private String idHechizo;
    private String nombre;
    private int rango;
    private int nivel;
    private String escuela;
    private String tiempoDeCasteo;
    private String duracion;
    private int concentracion;//es un boolean
    private int ritual;//es un boolean
    private int componenteVerbal;//es un boolean
    private int componenteSomatico;//es un boolean
    private int componenteMaterial;//es un boolean
    private String descripcionDelComponenteMaterial;
    private String descripcion;
    private String aMayorNivel;
    private ArrayList<Clase> clases;

    public Hechizo(String idHechizo, String nombre,String descripcion,String aMayorNivel,
                   int rango,int componenteVerbal, int componenteSomatico, int componenteMaterial,
                   String descripcionDelComponenteMaterial, int ritual, int concentracion,  String tiempoDeCasteo,
                   String escuela, int nivel, String duracion, ArrayList<Clase> clases) {
        this.idHechizo = idHechizo;
        this.nombre = nombre;
        this.rango = rango;
        this.nivel = nivel;
        this.escuela = escuela;
        this.tiempoDeCasteo = tiempoDeCasteo;
        this.duracion = duracion;
        this.concentracion = concentracion;
        this.ritual = ritual;
        this.componenteVerbal = componenteVerbal;
        this.componenteSomatico = componenteSomatico;
        this.componenteMaterial = componenteMaterial;
        this.descripcionDelComponenteMaterial = descripcionDelComponenteMaterial;
        this.descripcion = descripcion;
        this.aMayorNivel = aMayorNivel;
        this.clases=clases;
    }

    public String getIdHechizo() {
        return idHechizo;
    }

    public void setIdHechizo(String idHechizo) {
        this.idHechizo = idHechizo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getRango() {
        return rango;
    }

    public void setRango(int rango) {
        this.rango = rango;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public String getEscuela() {
        return escuela;
    }

    public void setEscuela(String escuela) {
        this.escuela = escuela;
    }

    public String getTiempoDeCasteo() {
        return tiempoDeCasteo;
    }

    public void setTiempoDeCasteo(String tiempoDeCasteo) {
        this.tiempoDeCasteo = tiempoDeCasteo;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public int getConcentracion() {
        return concentracion;
    }

    public void setConcentracion(int concentracion) {
        this.concentracion = concentracion;
    }

    public int getRitual() {
        return ritual;
    }

    public void setRitual(int ritual) {
        this.ritual = ritual;
    }

    public int getComponenteVerbal() {
        return componenteVerbal;
    }

    public void setComponenteVerbal(int componenteVerbal) {
        this.componenteVerbal = componenteVerbal;
    }

    public int getComponenteSomatico() {
        return componenteSomatico;
    }

    public void setComponenteSomatico(int componenteSomatico) {
        this.componenteSomatico = componenteSomatico;
    }

    public int getComponenteMaterial() {
        return componenteMaterial;
    }

    public void setComponenteMaterial(int componenteMaterial) {
        this.componenteMaterial = componenteMaterial;
    }

    public String getDescripcionDelComponenteMaterial() {
        return descripcionDelComponenteMaterial;
    }

    public void setDescripcionDelComponenteMaterial(String descripcionDelComponenteMaterial) {
        this.descripcionDelComponenteMaterial = descripcionDelComponenteMaterial;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getaMayorNivel() {
        return aMayorNivel;
    }

    public void setaMayorNivel(String aMayorNivel) {
        this.aMayorNivel = aMayorNivel;
    }

    public ArrayList<Clase> getClases() {
        return clases;
    }

    public void setClases(ArrayList<Clase> clases) {
        this.clases = clases;
    }
}
