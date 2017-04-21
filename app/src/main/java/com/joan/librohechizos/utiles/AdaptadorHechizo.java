package com.joan.librohechizos.utiles;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.joan.librohechizos.R;
import com.joan.librohechizos.modelo.Hechizo;
import com.joan.librohechizos.ui.ListarPersonajes;

import java.util.ArrayList;

/**
 * Created by Joan on 11/03/2017.
 */

public class AdaptadorHechizo extends ArrayAdapter<Hechizo> {

    AppCompatActivity appCompatActivity;
    ArrayList<Hechizo> lista;

    public AdaptadorHechizo(AppCompatActivity context, ArrayList<Hechizo> lista) {
        super(context, R.layout.icono_hechizo, lista);
        this.lista = lista;
        appCompatActivity = context;
    }

    public ArrayList<Hechizo> getListaActual() {
        return this.lista;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View item = convertView;
        Hechizo hechizo=lista.get(position);
        LayoutInflater inflater = appCompatActivity.getLayoutInflater();
        if(!hechizo.getNombre().equals("-1")){
        item = inflater.inflate(R.layout.icono_hechizo, null);
        TextView nombre = (TextView) item.findViewById(R.id.txt_mostrar_nombre_hechizo);
        nombre.setText(hechizo.getNombre());
        TextView Escuela = (TextView) item.findViewById(R.id.txt_hechizo_escuela);
        Escuela.setText(hechizo.getEscuela().getNombre());
        TextView nivel = (TextView) item.findViewById(R.id.txt_hechizo_nivel);
        if (hechizo.getNivel() == 0) {
            nivel.setText("Truco");
        } else {
            nivel.setText("Nivel: " + hechizo.getNivel());
        }
        if(hechizo.getRitual()==1){
            nombre.setText(nombre.getText()+" (RITUAL)" );
        }

        }else{
            item = inflater.inflate(R.layout.separador, null);
            TextView nivel = (TextView) item.findViewById(R.id.nivel);
            if (hechizo.getNivel() == 0) {
                nivel.setText("Truco");
            } else {
                nivel.setText("Nivel: " + hechizo.getNivel());
            }
        }

        return item;
    }

    public void setLista(ArrayList<Hechizo> list){
        this.lista=list;
        notifyDataSetChanged();
    }

}
