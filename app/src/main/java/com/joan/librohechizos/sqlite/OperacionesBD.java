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

import java.util.ArrayList;

/**

 */

public final class OperacionesBD {

    private static LibroHechizosBD baseDatos;

    private static OperacionesBD instancia = new OperacionesBD();

    private OperacionesBD() {

    }

    public static OperacionesBD obtenerInstancia(Context contexto) {

        baseDatos = new LibroHechizosBD(contexto);


        return instancia;
    }

    public Cursor obtenerDote(String id) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        String tablas = Tablas.DOTE;
        String[] proyeccion = {
                Dotes.ID_DOTE,
                Dotes.NOMBRE,
                Dotes.REQUISITOS,
                Dotes.DESCRIPCION};

        String where = Dotes.ID_DOTE + " = " + id;

        builder.setTables(tablas);

        Cursor resultado = builder.query(db, proyeccion, where, null, null, null, Dotes.NOMBRE);

        return resultado;

    }

    public Cursor obtenerDotes() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        String tablas = Tablas.DOTE;
        String[] proyeccion = {
                Dotes.ID_DOTE,
                Dotes.NOMBRE,
                Dotes.REQUISITOS};


        builder.setTables(tablas);
        Cursor resultado = builder.query(db, proyeccion, null, null, null, null, Dotes.NOMBRE);

        return resultado;

    }

    private boolean estaAprendidoDote(Dote dote, Personaje pj) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        String tablas = Tablas.DOTES_APRENDIDOS;
        String[] proyeccion = {
                Dotes.ID_DOTE,
                Personajes.ID_PERSONAJE};

        String where = Dotes.ID_DOTE + " = " + dote.getIdDote() +
                " AND " + Personajes.ID_PERSONAJE + " = " + pj.getIdPersonaje();

        builder.setTables(tablas);

        Cursor resultado = builder.query(db, proyeccion, where, null, null, null, null);

        return (resultado.getCount() > 0);
    }

    public Cursor obtenerDotesAprendidos(Personaje pj){
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        String a = Tablas.DOTES_APRENDIDOS;
        String b = Tablas.PERSONAJE ;
        String c = Tablas.DOTE ;
        String tablas = a + " INNER JOIN "+ b +
                            " ON " + a+"."+Personajes.ID_PERSONAJE +" = "+ b+"."+Personajes.ID_PERSONAJE+
                        " INNER JOIN " + c +
                             " ON "+ c+"."+Dotes.ID_DOTE +" = "+ a+"."+Dotes.ID_DOTE;
        String[] proyeccion = {
                Tablas.DOTE+"."+Dotes.ID_DOTE,
                Tablas.DOTE+"."+Dotes.NOMBRE,
                Tablas.DOTE+"."+Dotes.REQUISITOS};

        String WHERE = b+"."+Personajes.ID_PERSONAJE +" = "+ pj.getIdPersonaje();

        builder.setTables(tablas);
        Cursor resultado = builder.query(db, proyeccion, WHERE, null, null, null, Tablas.DOTE+"."+Dotes.NOMBRE);

        return resultado;
    }

    public void AprenderDote(Dote dote, Personaje pj) {
        if (!estaAprendidoDote(dote, pj)) {
            ContentValues valores = new ContentValues();
            valores.put(Dotes.ID_DOTE, dote.getIdDote());
            valores.put(Personajes.ID_PERSONAJE, pj.getIdPersonaje());
            getDb().insertOrThrow(Tablas.DOTES_APRENDIDOS, null, valores);
        }
    }

    public void eliminarDoteAprendido(Dote dote, Personaje pj) {
        if (estaAprendidoDote(dote, pj)) {
            String whereClause = Personajes.ID_PERSONAJE + " =? " + " AND " + Dotes.ID_DOTE + " =? ";
            String[] whereArgs = {pj.getIdPersonaje(),dote.getIdDote()};
            getDb().delete(Tablas.DOTES_APRENDIDOS, whereClause, whereArgs);

        }
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
        String WHERE = "id_personaje <> -1";

        builder.setTables(tablas);
        Cursor resultado = builder.query(db, proyeccion, WHERE, null, null, null, null);

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
        Cursor resultado = builder.query(db, proyeccion, null, null, null, null, Clases.NOMBRE);

        return resultado;
    }

    public long insertarClase(Clase clase) {
        ContentValues valores = new ContentValues();
        valores.put(Clases.NOMBRE, clase.getNombre());
        long idResultado = getDb().insertOrThrow(Tablas.CLASE, null, valores);
        return idResultado;
    }

    public Cursor obtenerHechizos(String filtro) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        String tablas = String.format("%s INNER JOIN %s ON %s=%s INNER JOIN %s ON %s=%s ",
                Tablas.HECHIZOS,
                Tablas.HECHIZOS_POR_CLASE, Tablas.HECHIZOS + "." + Hechizos.ID_HECHIZO, Tablas.HECHIZOS_POR_CLASE + "." + HechizosPorClases.ID_HECHIZO,
                Tablas.CLASE, Tablas.HECHIZOS_POR_CLASE + "." + HechizosPorClases.ID_CLASE, Tablas.CLASE + "." + Clases.ID_CLASE);
        String[] proyeccion = {
                Tablas.HECHIZOS + "." + Hechizos.ID_HECHIZO + " AS " + Tablas.HECHIZOS,
                Tablas.HECHIZOS + "." + Hechizos.NOMBRE,
                Tablas.HECHIZOS + "." + Hechizos.DESCRIPCION,
                Tablas.HECHIZOS + "." + Hechizos.A_MAYOR_NIVEL,
                Tablas.HECHIZOS + "." + Hechizos.RANGO,
                Tablas.HECHIZOS + "." + Hechizos.COMPONENTE_VERBAL,
                Tablas.HECHIZOS + "." + Hechizos.COMPONENTE_SOMATICO,
                Tablas.HECHIZOS + "." + Hechizos.COMPONENTE_MATERIAL,
                Tablas.HECHIZOS + "." + Hechizos.DESCRIPCION_COMPONENTE,
                Tablas.HECHIZOS + "." + Hechizos.RITUAL,
                Tablas.HECHIZOS + "." + Hechizos.CONCENTRACION,
                Tablas.HECHIZOS + "." + Hechizos.TIEMPO_DE_CASTEO,
                Tablas.HECHIZOS + "." + Hechizos.ESCUELA,
                Tablas.HECHIZOS + "." + Hechizos.NIVEL,
                Tablas.HECHIZOS + "." + Hechizos.DURACION
        };
        builder.setTables(tablas);
        builder.setDistinct(true);
        Cursor resultado = builder.query(db, proyeccion, filtro, null, null, null, Tablas.HECHIZOS + "." + Hechizos.NIVEL + "," + Tablas.HECHIZOS + "." + Hechizos.NOMBRE);

        return resultado;
    }

    public Cursor obtenerHechizos2(String filtro) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        String tablas = String.format("%s INNER JOIN %s ON %s=%s INNER JOIN %s ON %s=%s INNER JOIN %s ON %s=%s",
                Tablas.HECHIZOS,
                Tablas.HECHIZOS_POR_CLASE, Tablas.HECHIZOS + "." + Hechizos.ID_HECHIZO,
                Tablas.HECHIZOS_POR_CLASE + "." + HechizosPorClases.ID_HECHIZO,
                Tablas.CLASE, Tablas.HECHIZOS_POR_CLASE + "." + HechizosPorClases.ID_CLASE, Tablas.CLASE + "." + Clases.ID_CLASE,
                Tablas.ESCUELA, Tablas.HECHIZOS + "." + Hechizos.ESCUELA, Tablas.ESCUELA + "." + Escuelas.ID_ESCUELA);
        String[] proyeccion = {
                Tablas.HECHIZOS + "." + Hechizos.ID_HECHIZO + " AS " + Tablas.HECHIZOS,
                Tablas.HECHIZOS + "." + Hechizos.NOMBRE,
                Tablas.HECHIZOS + "." + Hechizos.RITUAL,
                Tablas.HECHIZOS + "." + Hechizos.NIVEL,
                Tablas.ESCUELA + "." + Escuelas.ID_ESCUELA,
                Tablas.ESCUELA + "." + Escuelas.NOMBRE,
        };
        builder.setTables(tablas);
        builder.setDistinct(true);
        Cursor resultado = builder.query(db, proyeccion, filtro, null, null, null, Tablas.HECHIZOS + "." + Hechizos.NIVEL + "," + Tablas.HECHIZOS + "." + Hechizos.NOMBRE);

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
        Cursor resultado = builder.query(db, proyeccion, null, null, null, null, Razas.NOMBRE);

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
                Tablas.CLASE + "." + Clases.NOMBRE,
                Tablas.CLASE + "." + Clases.ID_CLASE,
                Tablas.RAZA + "." + Razas.NOMBRE,
                Tablas.RAZA + "." + Razas.ID_RAZA
        };

        builder.setTables(tablas);
        Cursor resultado = builder.query(db, proyeccion, selection, selectionArgs, null, null, null);

        return resultado;

    }

    public Cursor obtenerHechizo(String idHechizo) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        String seleccion = String.format("%s =? ", Tablas.HECHIZOS + "." + Hechizos.ID_HECHIZO);
        String[] selectionArgs = {idHechizo};
        String tablas = String.format("%s INNER JOIN %s ON %s=%s INNER JOIN %s ON %s=%s ",
                Tablas.HECHIZOS,
                Tablas.HECHIZOS_POR_CLASE, Tablas.HECHIZOS + "." + Hechizos.ID_HECHIZO, Tablas.HECHIZOS_POR_CLASE + "." + HechizosPorClases.ID_HECHIZO,
                Tablas.CLASE, Tablas.HECHIZOS_POR_CLASE + "." + HechizosPorClases.ID_CLASE, Tablas.CLASE + "." + Clases.ID_CLASE);
        String[] proyeccion = {
                Tablas.HECHIZOS + "." + Hechizos.ID_HECHIZO + " AS " + Tablas.HECHIZOS,
                Tablas.HECHIZOS + "." + Hechizos.NOMBRE,
                Tablas.HECHIZOS + "." + Hechizos.DESCRIPCION,
                Tablas.HECHIZOS + "." + Hechizos.A_MAYOR_NIVEL,
                Tablas.HECHIZOS + "." + Hechizos.RANGO,
                Tablas.HECHIZOS + "." + Hechizos.COMPONENTE_VERBAL,
                Tablas.HECHIZOS + "." + Hechizos.COMPONENTE_SOMATICO,
                Tablas.HECHIZOS + "." + Hechizos.COMPONENTE_MATERIAL,
                Tablas.HECHIZOS + "." + Hechizos.DESCRIPCION_COMPONENTE,
                Tablas.HECHIZOS + "." + Hechizos.RITUAL,
                Tablas.HECHIZOS + "." + Hechizos.CONCENTRACION,
                Tablas.HECHIZOS + "." + Hechizos.TIEMPO_DE_CASTEO,
                Tablas.HECHIZOS + "." + Hechizos.ESCUELA,
                Tablas.HECHIZOS + "." + Hechizos.NIVEL,
                Tablas.HECHIZOS + "." + Hechizos.DURACION,
                Tablas.CLASE + "." + Clases.ID_CLASE,
                Tablas.CLASE + "." + Clases.NOMBRE
        };
        builder.setTables(tablas);
        Cursor resultado = builder.query(db, proyeccion, seleccion, selectionArgs, null, null, null);

        return resultado;
    }

    public Cursor obtenerEscuela(String idEscuela) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        String tablas = Tablas.ESCUELA;
        String[] proyeccion = {
                Escuelas.ID_ESCUELA,
                Escuelas.NOMBRE};
        String seleccion = String.format("%s=?", Escuelas.ID_ESCUELA);
        String[] seleccionArg = {idEscuela};
        builder.setTables(tablas);
        Cursor resultado = builder.query(db, proyeccion, seleccion, seleccionArg, null, null, null);
        return resultado;
    }

    public Cursor obtenerClasesDeHechizo(String idHechizo) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        String selection = String.format("%s=?", Tablas.HECHIZOS_POR_CLASE + "." + Hechizos.ID_HECHIZO);
        String[] selectionArgs = {idHechizo};
        String tablas = String.format("%s NATURAL JOIN %s",
                Tablas.HECHIZOS_POR_CLASE, Tablas.CLASE);
        String[] proyeccion = {
                Clases.ID_CLASE,
                Clases.NOMBRE};
        selection = selection;
        builder.setTables(tablas);
        Cursor resultado = builder.query(db, proyeccion, selection, selectionArgs, null, null, null);

        return resultado;
    }

    public Cursor obtenerHechizoAprendido(String idPersonaje, String selecFiltro) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        String tablas = String.format("%s INNER JOIN %s ON %s=%s INNER JOIN %s ON %s=%s " +
                        "INNER JOIN %s ON %s=%s INNER JOIN %s ON %s=%s",
                Tablas.PERSONAJE, Tablas.HECHIZOS_APRENDIDOS, Tablas.PERSONAJE + "." + Personajes.ID_PERSONAJE,
                Tablas.HECHIZOS_APRENDIDOS + "." + Personajes.ID_PERSONAJE,
                Tablas.HECHIZOS, Tablas.HECHIZOS_APRENDIDOS + "." + Hechizos.ID_HECHIZO, Tablas.HECHIZOS + "." + Hechizos.ID_HECHIZO,
                Tablas.HECHIZOS_POR_CLASE, Tablas.HECHIZOS + "." + Hechizos.ID_HECHIZO, Tablas.HECHIZOS_POR_CLASE + "." + HechizosPorClases.ID_HECHIZO,
                Tablas.CLASE, Tablas.HECHIZOS_POR_CLASE + "." + HechizosPorClases.ID_CLASE, Tablas.CLASE + "." + Clases.ID_CLASE);

        String[] proyeccion = {
                Tablas.HECHIZOS + "." + Hechizos.ID_HECHIZO + " AS " + Tablas.HECHIZOS,
                Tablas.HECHIZOS + "." + Hechizos.NOMBRE,
                Tablas.HECHIZOS + "." + Hechizos.DESCRIPCION,
                Tablas.HECHIZOS + "." + Hechizos.A_MAYOR_NIVEL,
                Tablas.HECHIZOS + "." + Hechizos.RANGO,
                Tablas.HECHIZOS + "." + Hechizos.COMPONENTE_VERBAL,
                Tablas.HECHIZOS + "." + Hechizos.COMPONENTE_SOMATICO,
                Tablas.HECHIZOS + "." + Hechizos.COMPONENTE_MATERIAL,
                Tablas.HECHIZOS + "." + Hechizos.DESCRIPCION_COMPONENTE,
                Tablas.HECHIZOS + "." + Hechizos.RITUAL,
                Tablas.HECHIZOS + "." + Hechizos.CONCENTRACION,
                Tablas.HECHIZOS + "." + Hechizos.TIEMPO_DE_CASTEO,
                Tablas.HECHIZOS + "." + Hechizos.ESCUELA,
                Tablas.HECHIZOS + "." + Hechizos.NIVEL,
                Tablas.HECHIZOS + "." + Hechizos.DURACION
        };
        String seleccion = String.format("%s=?", Tablas.PERSONAJE + "." + Personajes.ID_PERSONAJE);
        if (!selecFiltro.equals("")) {
            seleccion = seleccion + " AND " + selecFiltro;
        }
        String[] seleccionArg = {idPersonaje};
        builder.setTables(tablas);
        builder.setDistinct(true);
        Cursor resultado = builder.query(db, proyeccion, seleccion, seleccionArg, null, null, Tablas.HECHIZOS + "." + Hechizos.NIVEL + "," + Tablas.HECHIZOS + "." + Hechizos.NOMBRE);

        return resultado;
    }

    public Cursor obtenerHechizosAprendidos(String idPersonaje, String filtro) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        String tablas = String.format("%s INNER JOIN %s ON %s=%s INNER JOIN %s ON %s=%s " +
                        "INNER JOIN %s ON %s=%s INNER JOIN %s ON %s=%s INNER JOIN %s ON %s=%s",
                Tablas.PERSONAJE, Tablas.HECHIZOS_APRENDIDOS, Tablas.PERSONAJE + "." + Personajes.ID_PERSONAJE,
                Tablas.HECHIZOS_APRENDIDOS + "." + Personajes.ID_PERSONAJE,
                Tablas.HECHIZOS, Tablas.HECHIZOS_APRENDIDOS + "." + Hechizos.ID_HECHIZO, Tablas.HECHIZOS + "." + Hechizos.ID_HECHIZO,
                Tablas.HECHIZOS_POR_CLASE, Tablas.HECHIZOS + "." + Hechizos.ID_HECHIZO, Tablas.HECHIZOS_POR_CLASE + "." + HechizosPorClases.ID_HECHIZO,
                Tablas.CLASE, Tablas.HECHIZOS_POR_CLASE + "." + HechizosPorClases.ID_CLASE, Tablas.CLASE + "." + Clases.ID_CLASE,
                Tablas.ESCUELA, Tablas.HECHIZOS + "." + Hechizos.ESCUELA, Tablas.ESCUELA + "." + Escuelas.ID_ESCUELA);

        String[] proyeccion = {
                Tablas.HECHIZOS + "." + Hechizos.ID_HECHIZO + " AS " + Tablas.HECHIZOS,
                Tablas.HECHIZOS + "." + Hechizos.NOMBRE,
                Tablas.HECHIZOS + "." + Hechizos.RITUAL,
                Tablas.HECHIZOS + "." + Hechizos.NIVEL,
                Tablas.ESCUELA + "." + Escuelas.ID_ESCUELA,
                Tablas.ESCUELA + "." + Escuelas.NOMBRE,
        };
        builder.setTables(tablas);
        builder.setDistinct(true);
        String seleccion = String.format("%s=?", Tablas.PERSONAJE + "." + Personajes.ID_PERSONAJE);
        if (!filtro.equals("")) {
            seleccion = seleccion + " AND " + filtro;
        }
        String[] seleccionArg = {idPersonaje};
        Cursor resultado = builder.query(db, proyeccion, seleccion, seleccionArg, null, null, Tablas.HECHIZOS + "." + Hechizos.NIVEL + "," + Tablas.HECHIZOS + "." + Hechizos.NOMBRE);

        return resultado;
    }

    public Cursor obtenerHechizoPreparado(String idPersonaje, String selecFiltro) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        String tablas = String.format("%s INNER JOIN %s ON %s=%s INNER JOIN %s ON %s=%s " +
                        "INNER JOIN %s ON %s=%s INNER JOIN %s ON %s=%s",
                Tablas.PERSONAJE, Tablas.HECHIZOS_APRENDIDOS, Tablas.PERSONAJE + "." + Personajes.ID_PERSONAJE,
                Tablas.HECHIZOS_APRENDIDOS + "." + Personajes.ID_PERSONAJE,
                Tablas.HECHIZOS, Tablas.HECHIZOS_APRENDIDOS + "." + Hechizos.ID_HECHIZO, Tablas.HECHIZOS + "." + Hechizos.ID_HECHIZO,
                Tablas.HECHIZOS_POR_CLASE, Tablas.HECHIZOS + "." + Hechizos.ID_HECHIZO, Tablas.HECHIZOS_POR_CLASE + "." + HechizosPorClases.ID_HECHIZO,
                Tablas.CLASE, Tablas.HECHIZOS_POR_CLASE + "." + HechizosPorClases.ID_CLASE, Tablas.CLASE + "." + Clases.ID_CLASE);

        String[] proyeccion = {
                Tablas.HECHIZOS + "." + Hechizos.ID_HECHIZO + " AS " + Tablas.HECHIZOS,
                Tablas.HECHIZOS + "." + Hechizos.NOMBRE,
                Tablas.HECHIZOS + "." + Hechizos.DESCRIPCION,
                Tablas.HECHIZOS + "." + Hechizos.A_MAYOR_NIVEL,
                Tablas.HECHIZOS + "." + Hechizos.RANGO,
                Tablas.HECHIZOS + "." + Hechizos.COMPONENTE_VERBAL,
                Tablas.HECHIZOS + "." + Hechizos.COMPONENTE_SOMATICO,
                Tablas.HECHIZOS + "." + Hechizos.COMPONENTE_MATERIAL,
                Tablas.HECHIZOS + "." + Hechizos.DESCRIPCION_COMPONENTE,
                Tablas.HECHIZOS + "." + Hechizos.RITUAL,
                Tablas.HECHIZOS + "." + Hechizos.CONCENTRACION,
                Tablas.HECHIZOS + "." + Hechizos.TIEMPO_DE_CASTEO,
                Tablas.HECHIZOS + "." + Hechizos.ESCUELA,
                Tablas.HECHIZOS + "." + Hechizos.NIVEL,
                Tablas.HECHIZOS + "." + Hechizos.DURACION
        };
        String seleccion = String.format("%s=? AND %s=1", Tablas.PERSONAJE + "." + Personajes.ID_PERSONAJE,
                Tablas.HECHIZOS_APRENDIDOS + "." + HechizosAprendidos.PREPARADO);
        if (!selecFiltro.equals("")) {
            seleccion = seleccion + " AND " + selecFiltro;
        }
        ;
        String[] seleccionArg = {idPersonaje};
        builder.setTables(tablas);
        builder.setDistinct(true);
        Cursor resultado = builder.query(db, proyeccion, seleccion, seleccionArg, null, null, Tablas.HECHIZOS + "." + Hechizos.NIVEL + "," + Tablas.HECHIZOS + "." + Hechizos.NOMBRE);

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

    public void insertarHechizoAprendido(String idHechizo, String idPersonaje) {
        ContentValues valores = new ContentValues();
        valores.put(Personajes.ID_PERSONAJE, idPersonaje);
        valores.put(Hechizos.ID_HECHIZO, idHechizo);
        valores.put(HechizosAprendidos.PREPARADO, 0);
        getDb().insertOrThrow(Tablas.HECHIZOS_APRENDIDOS, null, valores);

    }

    public void dejarDePreparaHechizo(String idHechizo, String idPersonaje) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(HechizosAprendidos.PREPARADO, 0);
        String whereClause = String.format("%s=? AND %s=?", HechizosAprendidos.ID_PERSONAJE, HechizosAprendidos.ID_HECHIZO);
        String[] whereArgs = {idPersonaje, idHechizo};
        db.update(Tablas.HECHIZOS_APRENDIDOS, valores, whereClause, whereArgs);
    }

    public void prepararHechizo(String idPersonaje, String idHechizo) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(HechizosAprendidos.PREPARADO, 1);
        String whereClause = String.format("%s=? AND %s=?", HechizosAprendidos.ID_PERSONAJE, HechizosAprendidos.ID_HECHIZO);
        String[] whereArgs = {idPersonaje, idHechizo};

        db.update(Tablas.HECHIZOS_APRENDIDOS, valores, whereClause, whereArgs);
    }

    public void editarPersonaje(Personaje pj) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(Personajes.ID_CLASE, pj.getIdClase());
        valores.put(Personajes.NOMBRE, pj.getNombre());
        valores.put(Personajes.ID_RAZA, pj.getIdRaza());

        String selection = String.format("%s=? ",
                Personajes.ID_PERSONAJE);
        final String[] whereArgs = {pj.getIdPersonaje()};
        db.update(Tablas.PERSONAJE, valores, selection, whereArgs);

    }

    public void eliminarPersonaje(String idPersonaje) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        String whereClause = Personajes.ID_PERSONAJE + "=?";
        String[] whereArgs = {idPersonaje};
        db.delete(Tablas.PERSONAJE, whereClause, whereArgs);
    }

    public void eliminarHechizoAprendido(String idPersonaje, String idHechizo) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        String whereClause = String.format("%s=? AND %s=?", HechizosAprendidos.ID_PERSONAJE, HechizosAprendidos.ID_HECHIZO);
        String[] whereArgs = {idPersonaje, idHechizo};
        db.delete(Tablas.HECHIZOS_APRENDIDOS, whereClause, whereArgs);

    }

    public boolean estaAprendido(String idPersonaje, String idHechizo) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        String[] columns = {HechizosAprendidos.ID_HECHIZO, HechizosAprendidos.ID_PERSONAJE};
        String selection = HechizosAprendidos.ID_HECHIZO + " =? AND " + HechizosAprendidos.ID_PERSONAJE + "=?";
        String[] selectionArgs = {idHechizo, idPersonaje};
        String limit = "1";
        Cursor cursor = db.query(Tablas.HECHIZOS_APRENDIDOS, columns, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }


    public SQLiteDatabase getDb() {
        return baseDatos.getWritableDatabase();
    }


}