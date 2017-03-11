package com.joan.librohechizos.ui;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.joan.librohechizos.R;
import com.joan.librohechizos.modelo.Personaje;
import com.joan.librohechizos.sqlite.OperacionesBD;
import com.joan.librohechizos.utiles.AdaptadorPersonaje;

import java.util.ArrayList;

public class ListarPersonajes extends AppCompatActivity {
    private ArrayList<Personaje> lista;
    private OperacionesBD datos;
    private ListView listaPersonajes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listar_personaje);
        getApplicationContext().deleteDatabase("librohechizos.sqlite");
        datos = OperacionesBD.obtenerInstancia(getApplicationContext());
        lista=new ArrayList<Personaje>();
        listaPersonajes = (ListView)findViewById(R.id.list_personajes);
        cargarPersonajes();
        //Aca se define que se hace cuando se clikea un personaje
        listaPersonajes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ListarPersonajes.this, MostrarHechizo.class);
                startActivity(intent);
               // Intent intent = new Intent(ListarPersonajes.this,ListarHechizos.class);
               // intent.putExtra("idPersonaje",lista.get(i).getIdPersonaje());
            }
        });

    }

    private void cargarPersonajes(){
        obtenerPersonajes();
        AdaptadorPersonaje adaptador = new AdaptadorPersonaje(this,lista);
        listaPersonajes.setAdapter(adaptador);
    }


    protected void onResume() {
        super.onResume();
        cargarPersonajes();
    }

    public void btnCrearPersonaje(View view) {
        //Intent intent = new Intent(this, MostrarHechizo.class);
        //startActivity(intent);
        Intent intent = new Intent(this, CrearPersonajeActivity.class);
        startActivity(intent);
    }

    private void obtenerPersonajes(){
        Cursor listaPersonajes = datos.obtenerPersonajes();
        lista.clear();
        try {
            while (listaPersonajes.moveToNext()) {
                lista.add(new Personaje(listaPersonajes.getString(0),listaPersonajes.getString(1),listaPersonajes.getString(2),listaPersonajes.getString(3)));
            }
        }finally {
            listaPersonajes.close();

        }
    }
}
