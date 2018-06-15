package com.joan.librohechizos.modelo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import com.joan.librohechizos.sqlite.OperacionesBD;

import java.util.ArrayList;
import java.util.LinkedList;

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
    private ArrayList<Dote> dotes;

    public ArrayList<Dote> getDotes() {
        return dotes;
    }

    public void setDotes(ArrayList<Dote> dotes) {
        this.dotes = dotes;
    }

    public Personaje(String idPersonaje, String nombre, String idClase, String idRaza, Clase clase, Raza raza, ArrayList<Dote> dotes) {
        this.idPersonaje = idPersonaje;
        this.nombre = nombre;
        this.idClase = idClase;
        this.idRaza = idRaza;
        this.clase = clase;
        this.raza = raza;
        this.dotes = dotes;
    }

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
        Cursor cursorPersonajes = datos.obtenerPersonaje(pj.getIdPersonaje());
        try {
            if(cursorPersonajes.moveToNext()) {
                Clase clase = new Clase(cursorPersonajes.getString(3),
                        cursorPersonajes.getString(2));
                Raza raza =new Raza( cursorPersonajes.getString(5),
                        cursorPersonajes.getString(4));

                pj=new Personaje(cursorPersonajes.getString(0),
                        cursorPersonajes.getString(1), clase,raza);
            }
        }finally {
            cursorPersonajes.close();

        }
        return pj;
    }
    
    public static ArrayList<Personaje> obtenerTodos(Context contexto){
        OperacionesBD datos;
        datos = OperacionesBD.obtenerInstancia(contexto);
        Cursor cursorPersonajes = datos.obtenerPersonajes();
        ArrayList<Personaje> lista = new ArrayList<>();
        try {
            while (cursorPersonajes!=null && cursorPersonajes.moveToNext()) {
                lista.add(new Personaje(cursorPersonajes.getString(0),cursorPersonajes.getString(1),
                        cursorPersonajes.getString(2),cursorPersonajes.getString(3)));
            }
        }finally {
            cursorPersonajes.close();

        }
        return lista;
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

    public static void eliminar(Personaje pj,Context contexto){
        OperacionesBD datos;
        datos = OperacionesBD.obtenerInstancia(contexto);
        try {
            datos.getDb().beginTransaction();
            datos.eliminarPersonaje(pj.getIdPersonaje());
            datos.getDb().setTransactionSuccessful();
            Toast mensajeExito = Toast.makeText(contexto,
                    "Personaje eliminado correctamente",Toast.LENGTH_SHORT);
            mensajeExito.show();

        } finally {
            datos.getDb().endTransaction();
        }
    }

    public static void aprenderDote(Personaje pj,Dote dote,Context contexto){
        OperacionesBD datos;
        datos = OperacionesBD.obtenerInstancia(contexto);
        try {
            datos.getDb().beginTransaction();
            datos.AprenderDote(dote,pj);
            datos.getDb().setTransactionSuccessful();
            /*Toast mensajeExito = Toast.makeText(contexto,
                    "Dote aprendidos correctamente",Toast.LENGTH_SHORT);
            mensajeExito.show();*/

        } finally {
            datos.getDb().endTransaction();
        }
    }


    public static void olvidarDote(Personaje pj,Dote dote,Context contexto){
        OperacionesBD datos;
        datos = OperacionesBD.obtenerInstancia(contexto);
        try {
            datos.getDb().beginTransaction();
            datos.eliminarDoteAprendido(dote,pj);
            datos.getDb().setTransactionSuccessful();


        } finally {
            datos.getDb().endTransaction();
        }
    }
}
