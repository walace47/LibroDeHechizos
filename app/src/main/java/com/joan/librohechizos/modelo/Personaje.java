package com.joan.librohechizos.modelo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import com.joan.librohechizos.sqlite.OperacionesBD;

/**
 * Created by Giuliano on 06/03/2017.
 */

public class Personaje {

    private String idPersonaje;
    private String nombre;
    private String idClase;
    private String idRaza;
    private Clase clase;
    private Raza raza;

    public Personaje(String idPersonaje, String nombre, String idClase, String idRaza) {
        this.idPersonaje = idPersonaje;
        this.nombre = nombre;
        this.idClase = idClase;
        this.idRaza = idRaza;
    }

    public Personaje(String idPersonaje) {
        this.idPersonaje = idPersonaje;

    }

    public Personaje(String idPersonaje, String nombre, Clase clase, Raza raza) {
        this.idPersonaje = idPersonaje;
        this.nombre = nombre;
        this.clase = clase;
        this.raza = raza;
    }

    public Clase getClase() {
        return clase;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }

    public Raza getRaza() {
        return raza;
    }

    public void setRaza(Raza raza) {
        this.raza = raza;
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
    //Inserta un personaje
    public static boolean insertar(Personaje pj,Context contexto){
        OperacionesBD datos;
        datos = OperacionesBD.obtenerInstancia(contexto);
        boolean exito = true;
        datos.getDb().beginTransaction();
        try {
            datos.insertarPersonaje(pj);
            datos.getDb().setTransactionSuccessful();


        } catch (SQLiteException e){
            exito = false;
        } finally {
            datos.getDb().endTransaction();
        }
        return exito;
    }
    //obtiene los datos del personaje enviado
    public static Personaje obtener(Personaje pj,Context contexto){
        OperacionesBD datos;
        datos = OperacionesBD.obtenerInstancia(contexto);
        Cursor listaPersonajes = datos.obtenerPersonaje(pj.getIdPersonaje());
        try {
            if(listaPersonajes.moveToNext()) {
                Clase clase = new Clase(listaPersonajes.getString(3),
                        listaPersonajes.getString(2));
                Raza raza =new Raza( listaPersonajes.getString(5),
                        listaPersonajes.getString(4));

                pj=new Personaje(listaPersonajes.getString(0),
                        listaPersonajes.getString(1), clase,raza);
            }
        }finally {
            listaPersonajes.close();

        }
        return pj;
    }
    public static boolean editar(Personaje pj,Context contexto){
        OperacionesBD datos;
        datos = OperacionesBD.obtenerInstancia(contexto);
        boolean exitos = true;
        datos.getDb().beginTransaction();
        try{
            datos.editarPersonaje(pj);
            datos.getDb().setTransactionSuccessful();
        }catch (SQLiteException e) {
            exitos = false;
        }finally
         {
            datos.getDb().endTransaction();

        }
        return exitos;
    }
}
