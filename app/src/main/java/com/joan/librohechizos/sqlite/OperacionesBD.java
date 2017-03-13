package com.joan.librohechizos.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.joan.librohechizos.modelo.*;
import com.joan.librohechizos.sqlite.Contratos.*;
import com.joan.librohechizos.sqlite.LibroHechizosBD.*;

/**
 * Created by Giuliano on 06/03/2017.
 */

public final class OperacionesBD {

    private static LibroHechizosBD baseDatos;

    private static OperacionesBD instancia = new OperacionesBD();

    private OperacionesBD() {
    }

    public static OperacionesBD obtenerInstancia(Context contexto) {
        if (baseDatos == null) {
            baseDatos = new LibroHechizosBD(contexto);
        }
        return instancia;
    }

    public Cursor obtenerPersonajes() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        String tablas = String.format("%s INNER JOIN %s ON %s=%s INNER JOIN %s ON %s=%s",
                Tablas.PERSONAJE,
                Tablas.CLASE,
                Tablas.PERSONAJE + "." + Personajes.ID_CLASE,
                Tablas.CLASE + "." + Clases.ID_CLASE,
                Tablas.RAZA,
                Tablas.PERSONAJE + "." + Personajes.ID_RAZA,
                Tablas.RAZA + "." + Razas.ID_RAZA);
        String[] proyeccion = {
                Personajes.ID_PERSONAJE,
                Tablas.PERSONAJE + "." + Personajes.NOMBRE,
                Tablas.CLASE + "." + Clases.NOMBRE + " AS " + Tablas.CLASE,
                Tablas.RAZA + "." + Razas.NOMBRE + " AS " + Tablas.RAZA};

        builder.setTables(tablas);
        Cursor resultado = builder.query(db, proyeccion, null, null, null, null, null);

        return resultado;
    }

    public Cursor obtenerPersonajesAux() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        String tablas = Tablas.PERSONAJE;
        String[] proyeccion = {
                Personajes.ID_PERSONAJE,
                Personajes.NOMBRE,
                Personajes.ID_CLASE,
                Personajes.ID_RAZA};

        builder.setTables(tablas);
        Cursor resultado = builder.query(db, proyeccion, null, null, null, null, null);

        return resultado;
    }


    public Cursor obtenerClases() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        String tablas = Tablas.CLASE;
        String[] proyeccion = {
                Clases.ID_CLASE,
                Clases.NOMBRE};

        builder.setTables(tablas);
        Cursor resultado = builder.query(db, proyeccion, null, null, null, null, null);

        return resultado;
    }

    public long insertarClase(Clase clase) {
        ContentValues valores = new ContentValues();
        valores.put(Clases.NOMBRE, clase.getNombre());
        long idResultado = getDb().insertOrThrow(Tablas.CLASE, null, valores);
        return idResultado;
    }

    public Cursor obtenerHechizos() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        String tablas = Tablas.HECHIZOS;
        String[] proyeccion = {
                    Hechizos.ID_HECHIZO,
                    Hechizos.NOMBRE,
                    Hechizos.DESCRIPCION,
                    Hechizos.A_MAYOR_NIVEL,
                    Hechizos.RANGO,
                    Hechizos.COMPONENTE_VERBAL,
                    Hechizos.COMPONENTE_SOMATICO,
                    Hechizos.COMPONENTE_MATERIAL,
                    Hechizos.DESCRIPCION_COMPONENTE,
                    Hechizos.RITUAL,
                    Hechizos.CONCENTRACION,
                    Hechizos.TIEMPO_DE_CASTEO,
                    Hechizos.ESCUELA,
                    Hechizos.NIVEL,
                    Hechizos.DURACION
        };
        builder.setTables(tablas);
        Cursor resultado = builder.query(db, proyeccion, null, null, null, null, null);

        return resultado;
    }


    public Cursor obtenerRazas() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        String tablas = Tablas.RAZA;
        String[] proyeccion = {
                Razas.ID_RAZA,
                Razas.NOMBRE};

        builder.setTables(tablas);
        Cursor resultado = builder.query(db, proyeccion, null, null, null, null, null);

        return resultado;
    }

    public Cursor obtenerPersonaje(String id) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        String selection = String.format("%s=?", Personajes.ID_PERSONAJE);
        String[] selectionArgs = {id};
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        String tablas = String.format("%s INNER JOIN %s ON %s=%s INNER JOIN %s ON %s=%s",
                Tablas.PERSONAJE,
                Tablas.CLASE,
                Tablas.PERSONAJE + "." + Personajes.ID_CLASE,
                Tablas.CLASE + "." + Clases.ID_CLASE,
                Tablas.RAZA,
                Tablas.PERSONAJE + "." + Personajes.ID_RAZA,
                Tablas.RAZA + "." + Razas.ID_RAZA);
        String[] proyeccion = {
                Personajes.ID_PERSONAJE,
                Tablas.PERSONAJE + "." + Personajes.NOMBRE,
                Tablas.CLASE + "." + Clases.NOMBRE + " AS " + Tablas.CLASE,
                Tablas.RAZA + "." + Razas.NOMBRE + " AS " + Tablas.RAZA};

        builder.setTables(tablas);
        Cursor resultado = builder.query(db, proyeccion, selection, selectionArgs, null, null, null);

        return resultado;

    }

    public Cursor obtenerEscuela(String idEscuela){
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        String tablas= Tablas.ESCUELA;
        String[] proyeccion ={
                Escuelas.ID_ESCUELA,
                Escuelas.NOMBRE };
        String seleccion= String.format("%s=?",Escuelas.ID_ESCUELA);
        String[] seleccionArg={idEscuela};
        builder.setTables(tablas);
        Cursor resultado= builder.query(db,proyeccion,seleccion,seleccionArg,null,null,null);
        return resultado;
    }

    public Cursor obtenerClasesDeHechizo(String idHechizo){
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        String selection = String.format("%s=?", Tablas.HECHIZOS_POR_CLASE+"."+Hechizos.ID_HECHIZO);
        String[] selectionArgs = {idHechizo};
        String tablas = String.format("%s NATURAL JOIN %s",
                Tablas.HECHIZOS_POR_CLASE,Tablas.CLASE);
        String[] proyeccion = {
                Clases.ID_CLASE,
                Clases.NOMBRE};

        builder.setTables(tablas);
        Cursor resultado = builder.query(db, proyeccion, selection,selectionArgs, null, null, null);

        return resultado;
    }

    public Cursor obtenerHechizoAprendido(String idPersonaje){
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        String tablas = String.format("%s INNER JOIN %s ON %s=%s INNER JOIN %s ON %s=%s",
                Tablas.PERSONAJE,Tablas.HECHIZOS_APRENDIDOS,Tablas.PERSONAJE+"."+Personajes.ID_PERSONAJE,
                Tablas.HECHIZOS_APRENDIDOS+"."+Personajes.ID_PERSONAJE,
                Tablas.HECHIZOS,Tablas.HECHIZOS_APRENDIDOS+"."+Hechizos.ID_HECHIZO,Tablas.HECHIZOS+"."+Hechizos.ID_HECHIZO);
        String[] proyeccion = {
                Tablas.HECHIZOS+"."+Hechizos.ID_HECHIZO+" AS "+Tablas.HECHIZOS,
                Tablas.HECHIZOS+"."+Hechizos.NOMBRE,
                Tablas.HECHIZOS+"."+Hechizos.DESCRIPCION,
                Tablas.HECHIZOS+"."+Hechizos.A_MAYOR_NIVEL,
                Tablas.HECHIZOS+"."+Hechizos.RANGO,
                Tablas.HECHIZOS+"."+Hechizos.COMPONENTE_VERBAL,
                Tablas.HECHIZOS+"."+Hechizos.COMPONENTE_SOMATICO,
                Tablas.HECHIZOS+"."+Hechizos.COMPONENTE_MATERIAL,
                Tablas.HECHIZOS+"."+Hechizos.DESCRIPCION_COMPONENTE,
                Tablas.HECHIZOS+"."+Hechizos.RITUAL,
                Tablas.HECHIZOS+"."+Hechizos.CONCENTRACION,
                Tablas.HECHIZOS+"."+Hechizos.TIEMPO_DE_CASTEO,
                Tablas.HECHIZOS+"."+ Hechizos.ESCUELA,
                Tablas.HECHIZOS+"."+ Hechizos.NIVEL,
                Tablas.HECHIZOS+"."+ Hechizos.DURACION
        };
        String seleccion=String.format("%s=?",Tablas.PERSONAJE+"."+Personajes.ID_PERSONAJE);
        String[] seleccionArg={idPersonaje};
         builder.setTables(tablas);
        Cursor resultado = builder.query(db, proyeccion, seleccion, seleccionArg, null, null, null);

        return resultado;
    }

    public long insertarRaza(Raza raza) {
        ContentValues valores = new ContentValues();
        valores.put(Razas.NOMBRE, raza.getNombre());
        long idResultado = getDb().insertOrThrow(Tablas.RAZA, null, valores);
        return idResultado;
    }

    public long insertarPersonaje(Personaje personaje) {
        ContentValues valores = new ContentValues();
        valores.put(Personajes.NOMBRE, personaje.getNombre());
        valores.put(Personajes.ID_CLASE, personaje.getIdClase());
        valores.put(Personajes.ID_RAZA, personaje.getIdRaza());
        long idResultado = getDb().insertOrThrow(Tablas.PERSONAJE, null, valores);
        return idResultado;
    }

    public void aprenderHechizo(String idHechizo ,String idPersonaje){
        ContentValues valores = new ContentValues();
        int id= Integer.parseInt(idPersonaje);
        valores.put(Personajes.ID_PERSONAJE,idPersonaje);
        id=Integer.parseInt(idHechizo);
        valores.put(Hechizos.ID_HECHIZO,idHechizo);
        getDb().insertOrThrow(Tablas.HECHIZOS_APRENDIDOS,null,valores);

    }

    public void eliminarPersonaje(String idPersonaje){
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        String whereClause=Personajes.ID_PERSONAJE+"=?";
        String[] whereArgs = {idPersonaje};
        db.delete(Tablas.PERSONAJE, whereClause, whereArgs);
    }



    public SQLiteDatabase getDb() {
        return baseDatos.getWritableDatabase();
    }


}