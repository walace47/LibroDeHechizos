package com.joan.librohechizos.modelo;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.joan.librohechizos.sqlite.OperacionesBD;

import java.util.ArrayList;

/**
 * Created by Joan on 24/01/2018.
 */

public class Dote implements Parcelable{
    private String requisitos;
    private String idDote;
    private String descripcion;
    private String nombre;

    public Dote( String idDote,  String nombre,String requisitos,String descripcion) {
        this.requisitos = requisitos;
        this.idDote = idDote;
        this.descripcion = descripcion;
        this.nombre = nombre;
    }
    public Dote( String idDote,  String nombre,String requisitos) {
        this.requisitos = requisitos;
        this.idDote = idDote;
        this.nombre = nombre;
    }

    public Dote(String idDote, String nombre) {
        this.idDote = idDote;
        this.nombre = nombre;
    }

    protected Dote(Parcel in) {
        requisitos = in.readString();
        idDote = in.readString();
        descripcion = in.readString();
        nombre = in.readString();
    }

    public static final Creator<Dote> CREATOR = new Creator<Dote>() {
        @Override
        public Dote createFromParcel(Parcel in) {
            return new Dote(in);
        }

        @Override
        public Dote[] newArray(int size) {
            return new Dote[size];
        }
    };

    public String getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(String requisitos) {
        this.requisitos = requisitos;
    }

    public String getIdDote() {
        return idDote;
    }

    public void setIdDote(String idDote) {
        this.idDote = idDote;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(requisitos);
        dest.writeString(idDote);
        dest.writeString(descripcion);
        dest.writeString(nombre);
    }

    public static ArrayList<Dote> obtenerTodosLosDotes(Context contexto){
        OperacionesBD datos;
        datos = OperacionesBD.obtenerInstancia(contexto);
        Cursor cursorDotes = datos.obtenerDotes();
        ArrayList<Dote> dote = listarDotes(cursorDotes);
        return dote;
    }



    public static ArrayList<Dote> obtenerDotesAprendidos(Context contexto,Personaje pj){
        OperacionesBD datos;
        datos = OperacionesBD.obtenerInstancia(contexto);
        Cursor cursorDotes = datos.obtenerDotesAprendidos(pj);
        ArrayList<Dote> dote = listarDotes(cursorDotes);
        return dote;

    }

    private static  ArrayList<Dote> listarDotes(Cursor c){
        ArrayList<Dote> dote = new ArrayList<>();
        try{
            while (c.moveToNext()){
                dote.add(new Dote(c.getString(0),
                        c.getString(1),
                        c.getString(2)));
            }

        }finally {
            c.close();
        }
        return dote;
    }

    public static Dote obtenerDote(String id,Context contexto){
        OperacionesBD datos;
        datos = OperacionesBD.obtenerInstancia(contexto);
        Cursor cursorDote = datos.obtenerDote(id);
        Dote dote = null;
        try{
            if (cursorDote.moveToNext()){
                dote = new Dote(cursorDote.getString(0),
                        cursorDote.getString(1),
                        cursorDote.getString(2),
                        cursorDote.getString(3));
            }

            }finally {
                cursorDote.close();
        }
        return dote;
    }
}
