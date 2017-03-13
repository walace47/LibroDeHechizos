package com.joan.librohechizos.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.provider.BaseColumns;
import android.util.Log;

import com.joan.librohechizos.modelo.Clase;
import com.joan.librohechizos.modelo.Hechizo;
import com.joan.librohechizos.modelo.Personaje;
import com.joan.librohechizos.modelo.Raza;
import com.joan.librohechizos.sqlite.Contratos.*;

/**
 * Created by Giuliano on 06/03/2017.
 */

public class LibroHechizosBD extends SQLiteOpenHelper {

    private static final String NOMBRE_BASE_DATOS = "librohechizos.sqlite";

    private static final int VERSION_ACTUAL = 1;

    private final Context contexto;

    public LibroHechizosBD(Context context) {
        super(context, NOMBRE_BASE_DATOS, null, VERSION_ACTUAL);
        this.contexto = context;
    }

    interface Tablas {
        String PERSONAJE = "personaje";
        String CLASE = "clase";
        String RAZA = "raza";
        String HECHIZOS="hechizos";
        String HECHIZOS_APRENDIDOS="hechizos_aprendidos";
        String HECHIZOS_POR_CLASE="hechizos_por_clase";
        String ESCUELA="escuela";
    }

    interface Referencias {
        String ID_ESCUELA=String.format("REFERENCES %s(%s) ON UPDATE CASCADE ON DELETE CASCADE",
                Tablas.ESCUELA,Escuelas.ID_ESCUELA);

        String ID_PERSONAJE = String.format("REFERENCES %s(%s) ON UPDATE CASCADE ON DELETE CASCADE",
                Tablas.PERSONAJE, Personajes.ID_PERSONAJE);

        String ID_CLASE = String.format("REFERENCES %s(%s) ON UPDATE CASCADE ON DELETE CASCADE",
                Tablas.CLASE, Clases.ID_CLASE);

        String ID_RAZA = String.format("REFERENCES %s(%s) ON UPDATE CASCADE ON DELETE CASCADE",
                Tablas.RAZA, Razas.ID_RAZA);

        String ID_HECHIZOS=String.format("REFERENCES %s(%s) ON UPDATE CASCADE ON DELETE CASCADE",
                Tablas.HECHIZOS,Hechizos.ID_HECHIZO);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                db.setForeignKeyConstraintsEnabled(true);
            } else {
                db.execSQL("PRAGMA foreign_keys=ON");
            }
        }
    }

    private void precargarDatos(SQLiteDatabase db){
        try {
            //se cargan las clases
            db.beginTransaction();
            db.execSQL("INSERT INTO clase(nombre) values('guerrero')");
            db.execSQL("INSERT INTO clase(nombre) values('paladin')");
            db.execSQL("INSERT INTO clase(nombre) values('picaro')");
            db.execSQL("INSERT INTO clase(nombre) values('mago')");
            db.execSQL("INSERT INTO clase(nombre) values('hechicero')");
            db.execSQL("INSERT INTO clase(nombre) values('brujo')");
            db.execSQL("INSERT INTO clase(nombre) values('explorador')");
            db.execSQL("INSERT INTO clase(nombre) values('monje')");
            db.execSQL("INSERT INTO clase(nombre) values('druida')");
            db.execSQL("INSERT INTO clase(nombre) values('clerigo')");

            //se cargan las razas
            db.execSQL("INSERT INTO raza(nombre) values('draconido')");
            db.execSQL("INSERT INTO raza(nombre) values('humano')");
            db.execSQL("INSERT INTO raza(nombre) values('gnomo')");
            db.execSQL("INSERT INTO raza(nombre) values('mediano')");
            db.execSQL("INSERT INTO raza(nombre) values('enano')");
            db.execSQL("INSERT INTO raza(nombre) values('elfo')");
            db.execSQL("INSERT INTO raza(nombre) values('genazi')");
            //se carga personaje de prueba
            db.execSQL("INSERT INTO personaje(nombre,id_clase,id_raza) values('leonidas',3,2)");
            //se cargan escuelas
            db.execSQL(String.format("INSERT INTO %s(%s) values('conjuracion')",Tablas.ESCUELA, Escuelas.NOMBRE));
            //se cargan hechizos
            db.execSQL("INSERT INTO hechizos(nombre, descripcion, a_mayor_nivel, rango, componente_verbal, componente_somatico, componente_material, descripcion_componente, ritual, concentracion, tiempo_de_casteo, escuela, nivel, duracion) VALUES ('Zancada arbórea [Tree Stride]', 'Adquieres la habilidad de entrar en un árbol y moverte desde dentro del mismo hasta dentro de otro árbol del mismo tipo que se encuentre hasta a 500 pies (100 casillas, 150 m). Los dos árboles deben estar vivos y al menos tan grandes como tú. Debes usar 5 pies (1 casilla, 1.5m) de movimiento para entrar en el árbol. Conoces instantáneamente la localización de todos los árboles del mismo tipo a 500 pies (100 casillas, 150 m) de distancia y, como parte del movimiento usado para entrar en el árbol, puedes pasar dentro de uno de esos árboles o salir del árbol en el que estás. Apareces en un punto de tu elección a 5 pies (1 casilla, 1.5 m) del árbol destino, usando otros 5 pies (1 casilla, 1.5 m) de movimiento. Si no tienes suficiente movimiento, apareces a 5 pies (1 casilla, 1.5 m) del árbol en el que has entrado. Puedes usar esta habilidad de transporte una vez por asalto durante la duración. Debes terminar cada turno fuera de un árbol.', '', 0, 1, 1, 0, '', 0, 1, '1 acción', 1, 5, 'hasta 1 minuto')");
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL,%s INTEGER NOT NULL %s, %s INTEGER NOT NULL %s)",
                Tablas.PERSONAJE,
                Personajes.ID_PERSONAJE,
                Personajes.NOMBRE,
                Personajes.ID_CLASE, Referencias.ID_CLASE,
                Personajes.ID_RAZA, Referencias.ID_RAZA));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL)",
                Tablas.CLASE,
                Clases.ID_CLASE,
                Clases.NOMBRE));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL)",
                Tablas.RAZA,
                Razas.ID_RAZA,
                Razas.NOMBRE));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                "%s TEXT NOT NULL)",
                Tablas.ESCUELA,
                Escuelas.ID_ESCUELA,
                Escuelas.NOMBRE));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL,%s TEXT NOT NULL, %s TEXT NOT NULL ,%s INTEGER NOT NULL,"+
                        "%s INTEGER NOT NULL, %s INTEGER NOT NULL, %s INTEGER NOT NULL, %s TEXT NOT NULL," +
                        " %s INTEGER NOT NULL, %s INTEGER NOT NULL, %s TEXT NOT NULL, %s INTEGER NOT NULL %s, %s INTEGER NOT NULL, %s TEXT NOT NULL)",
                Tablas.HECHIZOS,
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
                Hechizos.ESCUELA,Referencias.ID_ESCUELA,
                Hechizos.NIVEL,
                Hechizos.DURACION));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER NOT NULL %s," +
                        "%s INTEGER NOT NULL %s, PRIMARY KEY(%s,%s))",
                Tablas.HECHIZOS_POR_CLASE,
                Clases.ID_CLASE,Referencias.ID_CLASE,
                Hechizos.ID_HECHIZO,Referencias.ID_HECHIZOS,
                Clases.ID_CLASE,Hechizos.ID_HECHIZO));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER NOT NULL %s, " +
                        "%s INTEGER NOT NULL %s, %s INTEGER NOT NULL , PRIMARY KEY(%s,%s))",
                Tablas.HECHIZOS_APRENDIDOS,
                HechizosAprendidos.ID_PERSONAJE, Referencias.ID_PERSONAJE,
                HechizosAprendidos.ID_HECHIZO,Referencias.ID_HECHIZOS,
                HechizosAprendidos.PREPARADO,
                Personajes.ID_PERSONAJE, Hechizos.ID_HECHIZO));
        precargarDatos(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion< oldVersion){
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.PERSONAJE);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.CLASE);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.RAZA);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.HECHIZOS);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.HECHIZOS_APRENDIDOS);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.HECHIZOS_POR_CLASE);
        }
    }



}
