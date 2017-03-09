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

import java.util.ArrayList;

public class ListarPersonajes extends AppCompatActivity {
    private ArrayList<Personaje> lista;
    private OperacionesBD datos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listar_personaje);
        getApplicationContext().deleteDatabase("librohechizos.sqlite");
        datos = OperacionesBD.obtenerInstancia(getApplicationContext());
        lista=new ArrayList<Personaje>();
        obtenerPersonajes();
        AdaptadorPersonaje adaptador = new AdaptadorPersonaje(this);
        ListView listaPersonajes = (ListView)findViewById(R.id.list_personajes);
        listaPersonajes.setAdapter(adaptador);
        //Aca se define que se hace cuando se clikea un personaje
        listaPersonajes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(ListarPersonajes.this,lista.get(i).getNombre(), Toast.LENGTH_LONG).show();
            }
        });

    }

    // es el adaptador para la lista de los personajes
    class AdaptadorPersonaje extends ArrayAdapter<Personaje> {

        AppCompatActivity appCompatActivity;

        AdaptadorPersonaje(AppCompatActivity context) {
            super(context, R.layout.personaje, lista);
            appCompatActivity = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = appCompatActivity.getLayoutInflater();
            View item = inflater.inflate(R.layout.personaje, null);
            TextView nombre = (TextView) item.findViewById(R.id.txt_personaje_nombre);
            nombre.setText(lista.get(position).getNombre());
            TextView clase = (TextView) item.findViewById(R.id.txt_clase);
            clase.setText(lista.get(position).getIdClase());
            TextView raza = (TextView) item.findViewById(R.id.txt_raza);
            raza.setText(lista.get(position).getIdRaza());
            return item;
        }
    }

    protected void onResume() {
        super.onResume();
        lista=new ArrayList<Personaje>();
        obtenerPersonajes();
        AdaptadorPersonaje adaptador = new AdaptadorPersonaje(this);
        ListView lv1 = (ListView)findViewById(R.id.list_personajes);
        lv1.setAdapter(adaptador);
    }

    public void btnCrearPersonaje(View view) {
        Intent intent = new Intent(this, CrearPersonajeActivity.class);
        startActivity(intent);
    }

    private void obtenerPersonajes(){
        Cursor listaPersonajes = datos.obtenerPersonajes();
        try {
            while (listaPersonajes.moveToNext()) {
                lista.add(new Personaje(listaPersonajes.getString(0),listaPersonajes.getString(1),listaPersonajes.getString(2),listaPersonajes.getString(3)));
            }
        }finally {
            listaPersonajes.close();

        }
    }
    //<activity android:name=".ui.ListarPersonajes"></activity>
}
