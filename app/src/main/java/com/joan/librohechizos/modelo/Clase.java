package com.joan.librohechizos.modelo;

import android.content.Context;
import android.database.Cursor;

import com.joan.librohechizos.Interfaces.Listable;
import com.joan.librohechizos.sqlite.OperacionesBD;

import java.util.LinkedList;



public class Clase implements Listable {

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
    @Override
    public String toString(){
        return this.nombre;
    }

    // Retorna las clases
    public static LinkedList<Listable> getClases(Context contexto){
        OperacionesBD datos;
        datos = OperacionesBD.obtenerInstancia(contexto);
        Cursor cursorClases;
        LinkedList<Listable> clases = new LinkedList<>();
        try {
            datos.getDb().beginTransaction();
            cursorClases = datos.obtenerClases();
            while (cursorClases.moveToNext()) {
                clases.add(new Clase(cursorClases.getString(0), cursorClases.getString(1)));
            }
            datos.getDb().setTransactionSuccessful();
        } finally {
            datos.getDb().endTransaction();
        }
       return clases;
    }

    public static void obtener(Clase clase){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Clase clase = (Clase) o;

        return idClase.equals(clase.idClase);
    }

}
