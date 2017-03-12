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
            instancia.precargarDatos();
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
        //int idPerosonaje=Integer.parseInt(id);
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

    public long insertarRaza(Raza raza) {
        ContentValues valores = new ContentValues();
        valores.put(Razas.NOMBRE, raza.getNombre());
        long idResultado = getDb().insertOrThrow(Tablas.RAZA, null, valores);
        return idResultado;
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

    public void precargarDatos() {
        try {
            getDb().beginTransaction();
            long idClase = insertarClase(new Clase("", "Guerrero"));
            Log.d("Clase nueva", "ID: " + idClase);
            idClase = insertarClase(new Clase("", "Paladin"));
            Log.d("Clase nueva", "ID: " + idClase);
            idClase = insertarClase(new Clase("", "Clerigo"));
            Log.d("Clase nueva", "ID: " + idClase);
            idClase = insertarClase(new Clase("", "Druida"));
            Log.d("Clase nueva", "ID: " + idClase);
            idClase = insertarClase(new Clase("", "Hechizero"));
            Log.d("Clase nueva", "ID: " + idClase);
            idClase = insertarClase(new Clase("", "Mago"));
            Log.d("Clase nueva", "ID: " + idClase);
            idClase = insertarClase(new Clase("", "Brujo"));
            Log.d("Clase nueva", "ID: " + idClase);
            //se cargaron las clases
            long idRaza = insertarRaza(new Raza("", "Humano"));
            Log.d("Raza nueva", "ID: " + idRaza);
            idRaza = insertarRaza(new Raza("", "Draconido"));
            Log.d("Raza nueva", "ID: " + idRaza);
            idRaza = insertarRaza(new Raza("", "Elfo"));
            Log.d("Raza nueva", "ID: " + idRaza);
            idRaza = insertarRaza(new Raza("", "Tieflin"));
            Log.d("Raza nueva", "ID: " + idRaza);
            idRaza = insertarRaza(new Raza("", "Gnomo"));
            Log.d("Raza nueva", "ID: " + idRaza);
            idRaza = insertarRaza(new Raza("", "Mediano"));
            Log.d("Raza nueva", "ID: " + idRaza);
            idRaza = insertarRaza(new Raza("", "Enano"));
            Log.d("Raza nueva", "ID: " + idRaza);
            idRaza = insertarRaza(new Raza("", "Genazi"));
            Log.d("Raza nueva", "ID: " + idRaza);
           long idPersonaje = insertarPersonaje(new Personaje("", "Leonidas", "1", "1"));
            Log.d("Personaje nuevo", "ID: " + idPersonaje);
            getDb().execSQL("INSERT INTO hechizos(nombre, descripcion, a_mayor_nivel, rango, componente_verbal, componente_somatico, componente_material, descripcion_componente, ritual, concentracion, tiempo_de_casteo, escuela, nivel, duracion) VALUES ('Zancada arbórea [Tree Stride]', 'Adquieres la habilidad de entrar en un árbol y moverte desde dentro del mismo hasta dentro de otro árbol del mismo tipo que se encuentre hasta a 500 pies (100 casillas, 150 m). Los dos árboles deben estar vivos y al menos tan grandes como tú. Debes usar 5 pies (1 casilla, 1.5m) de movimiento para entrar en el árbol. Conoces instantáneamente la localización de todos los árboles del mismo tipo a 500 pies (100 casillas, 150 m) de distancia y, como parte del movimiento usado para entrar en el árbol, puedes pasar dentro de uno de esos árboles o salir del árbol en el que estás. Apareces en un punto de tu elección a 5 pies (1 casilla, 1.5 m) del árbol destino, usando otros 5 pies (1 casilla, 1.5 m) de movimiento. Si no tienes suficiente movimiento, apareces a 5 pies (1 casilla, 1.5 m) del árbol en el que has entrado. Puedes usar esta habilidad de transporte una vez por asalto durante la duración. Debes terminar cada turno fuera de un árbol.', '', 0, 1, 1, 0, '', 0, 1, '1 acción', 'Conjuración', 5, 'hasta 1 minuto')");
            getDb().setTransactionSuccessful();
        } finally {
            getDb().endTransaction();
        }
    }
}