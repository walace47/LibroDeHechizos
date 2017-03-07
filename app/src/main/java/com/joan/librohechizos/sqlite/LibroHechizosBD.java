package com.joan.librohechizos.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.provider.BaseColumns;

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
    }

    interface Referencias {
        String ID_PERSONAJE = String.format("REFERENCES %s(%s) ON UPDATE CASCADE ON DELETE CASCADE",
                Tablas.PERSONAJE, Personajes.ID_PERSONAJE);

        String ID_CLASE = String.format("REFERENCES %s(%s) ON UPDATE CASCADE ON DELETE CASCADE",
                Tablas.CLASE, Clases.ID_CLASE);

        String ID_RAZA = String.format("REFERENCES %s(%s) ON UPDATE CASCADE ON DELETE CASCADE",
                Tablas.RAZA, Razas.ID_RAZA);
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.PERSONAJE);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.CLASE);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.RAZA);
        onCreate(db);
    }
}
