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
        imagen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast.makeText(getContext(), lista.get(position).getIdPersonaje(), Toast.LENGTH_SHORT).show();
            }
        });
        return item;
    }




}

