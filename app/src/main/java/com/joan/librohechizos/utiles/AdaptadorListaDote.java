package com.joan.librohechizos.utiles;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.joan.librohechizos.R;
import com.joan.librohechizos.modelo.Dote;
import com.joan.librohechizos.modelo.Hechizo;

import java.util.ArrayList;

/**
 * Created by Joan on 28/01/2018.
 */

public class AdaptadorListaDote extends ArrayAdapter<Dote> {

    protected AppCompatActivity appCompatActivity;
    protected ArrayList<Dote> lista;

    public AdaptadorListaDote(AppCompatActivity context, ArrayList<Dote> lista) {
        super(context, R.layout.icono_hechizo, lista);
        this.lista = lista;
        appCompatActivity = context;
    }


    public ArrayList<Dote> getListaActual() {
        return this.lista;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View item = convertView;
        Dote dote = lista.get(position);

        LayoutInflater inflater = appCompatActivity.getLayoutInflater();
        item = inflater.inflate(R.layout.icono_hechizo, null);

        TextView nombre = (TextView) item.findViewById(R.id.txt_mostrar_nombre_hechizo);
        nombre.setText(dote.getNombre());
        TextView Escuela = (TextView) item.findViewById(R.id.txt_hechizo_escuela);
        Escuela.setText(dote.getRequisitos());
        TextView nivel = (TextView) item.findViewById(R.id.txt_hechizo_nivel);
        nivel.setText("");


        return item;
    }

    public void setLista(ArrayList<Dote> list) {
        this.lista = list;
        notifyDataSetChanged();
    }

}
