package com.joan.librohechizos.utiles;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.joan.librohechizos.R;
import com.joan.librohechizos.modelo.Personaje;

import java.util.ArrayList;

/**
 * Created by Joan on 10/03/2017.
 */
public class AdaptadorPersonaje extends ArrayAdapter<Personaje> {

    AppCompatActivity appCompatActivity;
    ArrayList<Personaje> lista;

   public  AdaptadorPersonaje(AppCompatActivity context, ArrayList<Personaje> lista) {
        super(context, R.layout.icono_personaje, lista);
        this.lista=lista;
        appCompatActivity = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = appCompatActivity.getLayoutInflater();
        View item = inflater.inflate(R.layout.icono_personaje, null);
        TextView nombre = (TextView) item.findViewById(R.id.txt_personaje_nombre);
        nombre.setText(lista.get(position).getNombre());
        TextView clase = (TextView) item.findViewById(R.id.txt_clase);
        clase.setText(lista.get(position).getIdClase());
        TextView raza = (TextView) item.findViewById(R.id.txt_raza);
        raza.setText(lista.get(position).getIdRaza());
        return item;
    }
}

