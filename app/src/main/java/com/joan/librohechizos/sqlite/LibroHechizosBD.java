package com.joan.librohechizos.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * Created by Giuliano on 06/03/2017.
 */

public class LibroHechizosBD extends SQLiteOpenHelper {

    private static final String NOMBRE_BASE_DATOS = "librohechizos.sqlite";

    private static final int VERSION_ACTUAL = 10;

    private final Context contexto;

    public LibroHechizosBD(Context context) {
        super(context, NOMBRE_BASE_DATOS, null, VERSION_ACTUAL);
        this.contexto = context;
    }


    interface Tablas {
        String PERSONAJE = "personaje";
        String CLASE = "clase";
        String RAZA = "raza";
        String HECHIZOS = "hechizos";
        String HECHIZOS_APRENDIDOS = "hechizos_aprendidos";
        String HECHIZOS_POR_CLASE = "hechizos_por_clase";
        String ESCUELA = "escuela";
    }

    interface Referencias {
        String ID_ESCUELA = String.format("REFERENCES %s(%s) ON UPDATE CASCADE ON DELETE CASCADE",
                Tablas.ESCUELA, Escuelas.ID_ESCUELA);

        String ID_PERSONAJE = String.format("REFERENCES %s(%s) ON UPDATE CASCADE ON DELETE CASCADE",
                Tablas.PERSONAJE, Personajes.ID_PERSONAJE);

        String ID_CLASE = String.format("REFERENCES %s(%s) ON UPDATE CASCADE ON DELETE CASCADE",
                Tablas.CLASE, Clases.ID_CLASE);

        String ID_RAZA = String.format("REFERENCES %s(%s) ON UPDATE CASCADE ON DELETE CASCADE",
                Tablas.RAZA, Razas.ID_RAZA);

        String ID_HECHIZOS = String.format("REFERENCES %s(%s) ON UPDATE CASCADE ON DELETE CASCADE",
                Tablas.HECHIZOS, Hechizos.ID_HECHIZO);
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

    private void precargarDatos(SQLiteDatabase db) {
        try {
            db.beginTransaction();
            //se cargan las clases
            precargarClases(db);
            //se cargan las razas
            db.execSQL("INSERT INTO raza(nombre) values('Draconido')");
            db.execSQL("INSERT INTO raza(nombre) values('Humano')");
            db.execSQL("INSERT INTO raza(nombre) values('Gnomo')");
            db.execSQL("INSERT INTO raza(nombre) values('Mediano')");
            db.execSQL("INSERT INTO raza(nombre) values('Enano')");
            db.execSQL("INSERT INTO raza(nombre) values('Elfo')");
            db.execSQL("INSERT INTO raza(nombre) values('Genazi')");
            db.execSQL("INSERT INTO raza(nombre) values('Tiefling')");
            db.execSQL("INSERT INTO raza(nombre) values('Semi-orco')");
            db.execSQL("INSERT INTO raza(nombre) values('Semi-elfo')");
            db.execSQL("INSERT INTO raza(nombre) values('Kobold')");
            db.execSQL("INSERT INTO raza(nombre) values('Orco')");
            db.execSQL("INSERT INTO raza(nombre) values('Goliat')");
            //se carga personaje de prueba
            db.execSQL("INSERT INTO personaje(nombre,id_clase,id_raza) values('leonidas',3,2)");
            //se cargan escuelas
            db.execSQL(String.format("INSERT INTO %s(%s, %s) values(1,'conjuracion')", Tablas.ESCUELA, Escuelas.ID_ESCUELA, Escuelas.NOMBRE));
            db.execSQL(String.format("INSERT INTO %s(%s, %s) values(2,'evocacion')", Tablas.ESCUELA, Escuelas.ID_ESCUELA, Escuelas.NOMBRE));
            db.execSQL(String.format("INSERT INTO %s(%s, %s) values(3,'abjuracion')", Tablas.ESCUELA, Escuelas.ID_ESCUELA, Escuelas.NOMBRE));
            db.execSQL(String.format("INSERT INTO %s(%s, %s) values(4,'ilusion')", Tablas.ESCUELA, Escuelas.ID_ESCUELA, Escuelas.NOMBRE));
            db.execSQL(String.format("INSERT INTO %s(%s, %s) values(5,'encantamiento')", Tablas.ESCUELA, Escuelas.ID_ESCUELA, Escuelas.NOMBRE));
            db.execSQL(String.format("INSERT INTO %s(%s, %s) values(6,'necromancia')", Tablas.ESCUELA, Escuelas.ID_ESCUELA, Escuelas.NOMBRE));
            db.execSQL(String.format("INSERT INTO %s(%s, %s) values(7,'transmutacion')", Tablas.ESCUELA, Escuelas.ID_ESCUELA, Escuelas.NOMBRE));
            db.execSQL(String.format("INSERT INTO %s(%s, %s) values(8,'adivinacion')", Tablas.ESCUELA, Escuelas.ID_ESCUELA, Escuelas.NOMBRE));
            //se cargan hechizos

            precargarHechizoscsv(db,"assets/Hechizosl.csv");
            // db.execSQL("INSERT INTO " + Tablas.HECHIZOS_POR_CLASE + "(" + HechizosPorClases.ID_HECHIZO + "," + HechizosPorClases.ID_CLASE + ") VALUES (17,4)");
            //db.execSQL("INSERT INTO " + Tablas.HECHIZOS_POR_CLASE + "(" + HechizosPorClases.ID_HECHIZO + "," + HechizosPorClases.ID_CLASE + ") VALUES (17,5)");

            precargarHechizosXclaseCsv(db,"assets/HechizoxClase.csv");
            precargarHechizoscsvNuevos(db);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }


    public void precargarClases(SQLiteDatabase db) {
        InputStream inStream = getClass().getClassLoader().getResourceAsStream("assets/Clases.csv");
        BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
        String line = "";
        try {
            buffer.readLine();
            while ((line = buffer.readLine()) != null) {
                String[] colums = line.split(",");
                if (colums.length == 2) {
                    ContentValues cv = new ContentValues(2);
                    cv.put(Clases.ID_CLASE, colums[0].trim());
                    cv.put(Clases.NOMBRE, colums[1].trim());
                    db.insert(Tablas.CLASE, null, cv);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void precargarHechizoscsv(SQLiteDatabase db,String archivo) {
        InputStream inStream = getClass().getClassLoader().getResourceAsStream(archivo);
        BufferedReader buffer = null;
        try {
            buffer = new BufferedReader(new InputStreamReader(inStream, "cp1252"));
        } catch (UnsupportedEncodingException e) {
            System.out.printf("no anda");
            e.printStackTrace();
        }
        String line = "";
        try {
            while ((line = buffer.readLine()) != null) {
                String[] colums = line.split("&");
                if (colums.length == 15) {
                    ContentValues cv = new ContentValues();
                    cv.put(Hechizos.ID_HECHIZO, colums[0].trim());
                    cv.put(Hechizos.NOMBRE, colums[1].trim());
                    String descripcion = colums[2].trim();
                    descripcion = descripcion.replaceAll("<br>", "</p><p>");
                    cv.put(Hechizos.DESCRIPCION, descripcion);
                    cv.put(Hechizos.A_MAYOR_NIVEL, colums[3].trim());
                    cv.put(Hechizos.RANGO, colums[4].trim());
                    cv.put(Hechizos.COMPONENTE_VERBAL, colums[5].trim());
                    cv.put(Hechizos.COMPONENTE_SOMATICO, colums[6].trim());
                    cv.put(Hechizos.COMPONENTE_MATERIAL, colums[7].trim());
                    cv.put(Hechizos.DESCRIPCION_COMPONENTE, colums[8].trim());
                    cv.put(Hechizos.RITUAL, colums[9].trim());
                    cv.put(Hechizos.CONCENTRACION, colums[10].trim());
                    cv.put(Hechizos.TIEMPO_DE_CASTEO, colums[11].trim());
                    cv.put(Hechizos.ESCUELA, colums[12].trim());
                    cv.put(Hechizos.NIVEL, colums[13].trim());
                    cv.put(Hechizos.DURACION, colums[14].trim());
                    db.insert(Tablas.HECHIZOS, null, cv);

                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void precargarHechizoscsvNuevos(SQLiteDatabase db) {
        HashMap<String, String[]> mapa = new HashMap<>();
        InputStream inStream = getClass().getClassLoader().getResourceAsStream("assets/HechizosElemental.csv");
        BufferedReader buffer = null;
        try {
            buffer = new BufferedReader(new InputStreamReader(inStream, "cp1252"));
        } catch (UnsupportedEncodingException e) {
            System.out.printf("no anda");
            e.printStackTrace();
        }
        String line = "";
        try {
            while ((line = buffer.readLine()) != null) {
                String[] colums = line.split(";");
                if (colums.length == 9) {
                    String[] arregloHechizo;
                    String componente_V = "0", componente_M = "0", componente_S = "0", accion, distancia, nombre,
                            nivel, escuela, ritual = "0", concentracion = "0", duracion, descripcion, aNuevoNivel = "", material = "", clase;
                    nombre = colums[1].trim();
                    nombre = nombre.toLowerCase();
                    Character primer = nombre.charAt(0);
                    nombre = primer.toString().toUpperCase() + nombre.substring(1,nombre.length());
                    if (nombre.contains("RITUAL")) {
                        ritual = "1";
                        nombre = nombre.substring(0, nombre.indexOf("(") - 1);
                    }
                    if (!mapa.containsKey(nombre)) {
                        nivel = colums[0].trim();
                        escuela = colums[2].trim();
                        if (escuela.contains("Conjuración")) {
                            escuela = "1";
                        } else if (escuela.contains("Evocación")) {
                            escuela = "2";
                        } else if (escuela.contains("Abjuración")) {
                            escuela = "3";
                        } else if (escuela.contains("Ilusión")) {
                            escuela = "4";
                        } else if (escuela.contains("Encantamiento")) {
                            escuela = "5";
                        } else if (escuela.contains("Nigromancia")) {
                            escuela = "6";
                        } else if (escuela.contains("Transmutación")) {
                            escuela = "7";
                        } else if (escuela.contains("Adivinación")) {
                            escuela = "8";
                        }
                        accion = colums[3].trim();
                        distancia = colums[4].trim();
                        if (distancia.contains("pies")) {
                            distancia = distancia.substring(0, distancia.indexOf('p') - 1);
                        } else {
                            if (distancia.contains("Personal")) {
                                distancia = "0";
                            } else {
                                if (distancia.contains("Contacto")) {
                                    distancia = "1";

                                } else {
                                    distancia = "-1";
                                }
                            }
                        }
                        if (colums[5].contains("V")) {
                            componente_V = "1";
                        }
                        if (colums[5].contains("M")) {
                            componente_M = "1";
                        }
                        if (colums[5].contains("S")) {
                            componente_S = "1";
                        }
                        duracion = colums[6].trim();
                        if (duracion.contains("Concentración")) {
                            duracion = duracion.replace("Concentración, ", "");
                            concentracion = "1";
                        }
                        descripcion = colums[7].trim();
                        if (componente_M.equals("1")) {
                            material = descripcion.substring(descripcion.indexOf("("), descripcion.indexOf(")"));
                            descripcion = descripcion.substring(descripcion.indexOf(")")+1);
                        }
                        if (descripcion.contains("El daño del conjuro se incrementa")) {
                            aNuevoNivel = descripcion.substring(descripcion.indexOf("El daño del conjuro se incrementa"));
                            descripcion = descripcion.substring(0, descripcion.indexOf("El daño del conjuro se incrementa") - 8);
                        }else if(descripcion.contains("A niveles superiores.")){
                            aNuevoNivel = descripcion.substring(descripcion.indexOf("A niveles superiores."));
                            aNuevoNivel = aNuevoNivel.replace("A niveles superiores.","");
                            descripcion = descripcion.substring(0, descripcion.indexOf("A niveles superiores") - 8);
                        }
                        descripcion = "<p> " + descripcion + " </p>";
                        descripcion = descripcion.replaceAll("<br><br>", "</p><p>");
                        descripcion = descripcion.replaceAll("<BR><BR>", "</p><p>");
                        descripcion = descripcion.replaceAll("<BR><br>", "</p><p>");
                        descripcion = descripcion.replaceAll("<br><BR>", "</p><p>");
                        descripcion = descripcion.replaceAll("<BR>", "</p><p>");
                        descripcion = descripcion.replaceAll("<br>", "</p><p>");
                        descripcion.trim();
                        //clase
                        clase = colums[8].trim();
                        if (clase.contains("Guerrero")) {
                            clase = "1";
                        } else if (clase.contains("Paladin")) {
                            clase = "2";
                        } else if (clase.contains("Picaro")) {
                            clase = "3";
                        } else if (clase.contains("Mago")) {
                            clase = "4";
                        } else if (clase.contains("Hechicero")) {
                            clase = "5";
                        } else if (clase.contains("brujo")) {
                            clase = "6";
                        } else if (clase.contains("Explorador")) {
                            clase = "7";
                        } else if (clase.contains("Monje")) {
                            clase = "8";
                        } else if (clase.contains("Druida")) {
                            clase = "9";
                        } else if (clase.contains("Clerigo")) {
                            clase = "10";
                        } else if (clase.contains("Bardo")) {
                            clase = "11";
                        } else if (clase.contains("Barbaro")) {
                            clase = "12";
                        }
                        arregloHechizo = new String[]{nombre, descripcion, aNuevoNivel, distancia,
                                componente_V, componente_S, componente_M, material, ritual, concentracion, accion,
                                escuela, nivel, duracion, clase};
                        mapa.put(nombre, arregloHechizo);
                    } else {
                        String[] hechizo = mapa.get(nombre);
                        clase = colums[8].trim();
                        if (clase.contains("Guerrero")) {
                            hechizo[14] = hechizo[14]+ ",1";
                        } else if (clase.contains("Paladin")) {
                            hechizo[14] = hechizo[14] + ",2";
                        } else if (clase.contains("Picaro")) {
                            hechizo[14] = hechizo[14]+ ",3";
                        } else if (clase.contains("Mago")) {
                            hechizo[14] = hechizo[14]+ ",4";
                        } else if (clase.contains("Hechicero")) {
                            hechizo[14] = hechizo[14]+ ",5";
                        } else if (clase.contains("brujo")) {
                            hechizo[14] = hechizo[14]+ ",6";
                        } else if (clase.contains("Explorador")) {
                            hechizo[14] = hechizo[14]+ ",7";
                        } else if (clase.contains("Monje")) {
                            hechizo[14] = hechizo[14]+ ",8";
                        } else if (clase.contains("Druida")) {
                            hechizo[14] = hechizo[14]+ ",9";
                        } else if (clase.contains("Clerigo")) {
                            hechizo[14] = hechizo[14]+ ",10";
                        } else if (clase.contains("Bardo")) {
                            hechizo[14] = hechizo[14]+ ",11";
                        } else if (clase.contains("Barbaro")) {
                            hechizo[14] = hechizo[14]+ ",12";
                        }
                        //hechizo[14] = clase;
                        //mapa.remove(hechizo[0]);
                      //  mapa.put(hechizo[0], hechizo);
                    }


                }


            }
            int i = 800;
            for (String[] value : mapa.values()) {
                ContentValues cv = new ContentValues();
                cv.put(Hechizos.ID_HECHIZO, i);
                cv.put(Hechizos.NOMBRE, value[0].trim());
                String descripcion = value[1].trim();
                cv.put(Hechizos.DESCRIPCION, descripcion);
                cv.put(Hechizos.A_MAYOR_NIVEL, value[2].trim());
                cv.put(Hechizos.RANGO, value[3].trim());
                cv.put(Hechizos.COMPONENTE_VERBAL, value[4].trim());
                cv.put(Hechizos.COMPONENTE_SOMATICO, value[5].trim());
                cv.put(Hechizos.COMPONENTE_MATERIAL, value[6].trim());
                cv.put(Hechizos.DESCRIPCION_COMPONENTE, value[7].trim());
                cv.put(Hechizos.RITUAL, value[8].trim());
                cv.put(Hechizos.CONCENTRACION, value[9].trim());
                cv.put(Hechizos.TIEMPO_DE_CASTEO, value[10].trim());
                cv.put(Hechizos.ESCUELA, value[11].trim());
                cv.put(Hechizos.NIVEL, value[12].trim());
                cv.put(Hechizos.DURACION, value[13].trim());
                db.insert(Tablas.HECHIZOS, null, cv);
                String[] clases = value[14].split(",");
                cv.clear();
                for (int j = 0; j < clases.length; j++){
                    cv.put(HechizosPorClases.ID_HECHIZO, i);
                    cv.put(HechizosPorClases.ID_CLASE, clases[j].trim());
                    db.insert(Tablas.HECHIZOS_POR_CLASE, null, cv);
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void precargarHechizosXclaseCsv(SQLiteDatabase db,String archivo) {
        InputStream inStream = getClass().getClassLoader().getResourceAsStream(archivo);
        BufferedReader buffer = null;
        try {
            buffer = new BufferedReader(new InputStreamReader(inStream, "Cp1252"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String line = "";
        try {
            while ((line = buffer.readLine()) != null) {
                String[] colums = line.split("&");
                ContentValues cv = new ContentValues();
                cv.put(HechizosPorClases.ID_HECHIZO, colums[0].trim());
                cv.put(HechizosPorClases.ID_CLASE, colums[1].trim());
                db.insert(Tablas.HECHIZOS_POR_CLASE, null, cv);
            }
        } catch (IOException e) {
            e.printStackTrace();
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
                        "%s TEXT NOT NULL,%s TEXT NOT NULL, %s TEXT NOT NULL ,%s INTEGER NOT NULL," +
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
                Hechizos.ESCUELA, Referencias.ID_ESCUELA,
                Hechizos.NIVEL,
                Hechizos.DURACION));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER NOT NULL %s," +
                        "%s INTEGER NOT NULL %s, PRIMARY KEY(%s,%s))",
                Tablas.HECHIZOS_POR_CLASE,
                HechizosPorClases.ID_CLASE, Referencias.ID_CLASE,
                HechizosPorClases.ID_HECHIZO, Referencias.ID_HECHIZOS,
                Clases.ID_CLASE, Hechizos.ID_HECHIZO));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER NOT NULL %s, " +
                        "%s INTEGER NOT NULL %s, %s INTEGER NOT NULL , PRIMARY KEY(%s,%s))",
                Tablas.HECHIZOS_APRENDIDOS,
                HechizosAprendidos.ID_PERSONAJE, Referencias.ID_PERSONAJE,
                HechizosAprendidos.ID_HECHIZO, Referencias.ID_HECHIZOS,
                HechizosAprendidos.PREPARADO,
                Personajes.ID_PERSONAJE, Hechizos.ID_HECHIZO));
        precargarDatos(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (newVersion > oldVersion && oldVersion == 8) {
            precargarHechizoscsvNuevos(db);
        }
        if (newVersion > oldVersion && oldVersion == 9){
            precargarHechizoscsv(db,"assets/heroismo.txt");
            precargarHechizosXclaseCsv(db,"assets/clases.txt");
        }
    }


}
