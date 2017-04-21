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
import com.joan.librohechizos.modelo.Clase;
import com.joan.librohechizos.modelo.Personaje;
import com.joan.librohechizos.ui.ListarPersonajes;

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
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View item = convertView;
        LayoutInflater inflater = appCompatActivity.getLayoutInflater();
        item=inflater.inflate(R.layout.icono_personaje,null);
        TextView nombre = (TextView) item.findViewById(R.id.txt_personaje_nombre);
        nombre.setText(lista.get(position).getNombre());
        TextView clase = (TextView) item.findViewById(R.id.txt_clase);
        clase.setText(lista.get(position).getIdClase());
        TextView raza = (TextView) item.findViewById(R.id.txt_raza);
        raza.setText(lista.get(position).getIdRaza());
        ImageView imagen=(ImageView) item.findViewById(R.id.imageView);
        switch (lista.get(position).getIdClase()){
            case("Paladin"):imagen.setImageResource(R.drawable.paladin);
                break;
            case("Picaro"):imagen.setImageResource(R.drawable.picaro);
                break;
            case("Guerrero"):imagen.setImageResource(R.drawable.guerrero);
                break;
            case("Mago"):imagen.setImageResource(R.drawable.mago);
                break;
            case("Hechicero"):imagen.setImageResource(R.drawable.hechicero);
                break;
            case("Brujo"):imagen.setImageResource(R.drawable.brujo);
                break;
            case("Explorador"):imagen.setImageResource(R.drawable.explorador);
                break;
            case("Monje"):imagen.setImageResource(R.drawable.monje);
                break;
            case("Druida"):imagen.setImageResource(R.drawable.druida);
                break;
            case("Clerigo"):imagen.setImageResource(R.drawable.clerigo);
                break;
            case("Bardo"):imagen.setImageResource(R.drawable.bardo);
                break;
            case("Barbaro"):imagen.setImageResource(R.drawable.barbaro);
                break;
        }
        return item;
    }




}

