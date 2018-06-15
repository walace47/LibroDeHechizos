package com.joan.librohechizos.modelo;

import android.content.Context;
import android.database.Cursor;

import com.joan.librohechizos.Interfaces.Listable;
import com.joan.librohechizos.sqlite.OperacionesBD;

import java.util.LinkedList;

/**
 * Created by Giuliano on 07/03/2017.
 */

public class Raza implements Listable{

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
    @Override
    public String toString(){
        return nombre;
    }
    
    public static LinkedList<Listable> getRazas(Context contexto){
        OperacionesBD datos;
        datos = OperacionesBD.obtenerInstancia(contexto);
        Cursor cursorRazas;
        LinkedList<Listable> razas = new LinkedList<>();
        try {
            datos.getDb().beginTransaction();
            cursorRazas = datos.obtenerRazas();
            while (cursorRazas.moveToNext()) {
                razas.add(new Raza(cursorRazas.getString(0), cursorRazas.getString(1)));
            }
            datos.getDb().setTransactionSuccessful();
        } finally {
            datos.getDb().endTransaction();
        }
        return razas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Raza raza = (Raza) o;

        return idRaza.equals(raza.idRaza);
    }

}
