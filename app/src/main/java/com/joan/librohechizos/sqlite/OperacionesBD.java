package com.joan.librohechizos.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

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
                Tablas.PERSONAJE+"."+Personajes.ID_CLASE,
                Tablas.CLASE+"."+Clases.ID_CLASE,
                Tablas.RAZA,
                Tablas.PERSONAJE+"."+Personajes.ID_RAZA,
                Tablas.RAZA+"."+Razas.ID_RAZA);
        String[] proyeccion = {
                Personajes.ID_PERSONAJE,
                Tablas.PERSONAJE + "." + Personajes.NOMBRE,
                Tablas.CLASE + "." + Clases.NOMBRE+" AS "+Tablas.CLASE,
                Tablas.RAZA + "." + Razas.NOMBRE+" AS "+Tablas.RAZA};

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

    public long insertarPersonaje(Personaje personaje) {
        ContentValues valores = new ContentValues();
        valores.put(Personajes.NOMBRE, personaje.getNombre());
        valores.put(Personajes.ID_CLASE, personaje.getIdClase());
        valores.put(Personajes.ID_RAZA, personaje.getIdRaza());
        long idResultado = getDb().insertOrThrow(Tablas.PERSONAJE, null, valores);
        return idResultado;
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

    public Cursor obtenerClases(String nombre) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        String selection = String.format("%s=?", Clases.NOMBRE);
        String[] selectionArgs = {nombre};
        String tablas = Tablas.CLASE;
        String[] proyeccion = {
                Clases.ID_CLASE,
                Clases.NOMBRE};

        builder.setTables(tablas);
        Cursor resultado = builder.query(db, proyeccion, selection, selectionArgs, null, null, null);

        return resultado;
    }

    public long insertarClase(Clase clase) {
        ContentValues valores = new ContentValues();
        valores.put(Clases.NOMBRE, clase.getNombre());
        long idResultado = getDb().insertOrThrow(Tablas.CLASE, null, valores);
        return idResultado;
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

    public Cursor obtenerRazas(String nombre) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        String selection = String.format("%s=?", Razas.NOMBRE);
        String[] selectionArgs = {nombre};
        String tablas = Tablas.RAZA;
        String[] proyeccion = {
                Razas.ID_RAZA,
                Razas.NOMBRE};

        builder.setTables(tablas);
        Cursor resultado = builder.query(db, proyeccion,selection, selectionArgs, null, null, null);

        return resultado;
    }

    public long insertarRaza(Raza raza) {
        ContentValues valores = new ContentValues();
        valores.put(Razas.NOMBRE, raza.getNombre());
        long idResultado = getDb().insertOrThrow(Tablas.RAZA, null, valores);
        return idResultado;
    }

    public SQLiteDatabase getDb() {
        return baseDatos.getWritableDatabase();
    }
}