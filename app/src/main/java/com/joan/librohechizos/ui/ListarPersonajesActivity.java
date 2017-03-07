package com.joan.librohechizos.ui;

import android.content.Intent;
import android.database.DatabaseUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.joan.librohechizos.R;
import com.joan.librohechizos.modelo.*;
import com.joan.librohechizos.sqlite.OperacionesBD;

/**
 * Created by Giuliano on 06/03/2017.
 */

public class ListarPersonajesActivity extends AppCompatActivity {

    private OperacionesBD datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_personajes);

        //Esto hace que la base de datos se borre cada vez que ejecutas la app
        getApplicationContext().deleteDatabase("librohechizos.sqlite");

        datos = OperacionesBD.obtenerInstancia(getApplicationContext());

        //Carga datos para consultar
        precargarDatos();

        //Consulta todas las clases y las muestra por la consola (no muestra en la app)
        Log.d("Clases","Lista de clases");
        DatabaseUtils.dumpCursor(datos.obtenerClases());

        //Consulta todas las razas y las muestra por la consola (no muestra en la app)
        Log.d("Razas","Lista de razas");
        DatabaseUtils.dumpCursor(datos.obtenerRazas());

        //Consulta todos los personajes y los muestra por la consola (no muestra en la app)
        Log.d("Personajes","Lista de personajes");
        DatabaseUtils.dumpCursor(datos.obtenerPersonajes());
    }

    public void btnCrearPersonaje() {
        Intent intent = new Intent(ListarPersonajesActivity.this, CrearPersonaje.class);
        startActivity(intent);
    }

    private void precargarDatos() {
        try {
            datos.getDb().beginTransaction();
            long idClase = datos.insertarClase(new Clase("","Guerrero"));
            Log.d("Clase nueva","ID: "+idClase);
            long idRaza = datos.insertarRaza(new Raza("","Humano"));
            Log.d("Raza nueva","ID: "+idRaza);
            long idPersonaje = datos.insertarPersonaje(new Personaje("","Leonidas","1","1"));
            Log.d("Personaje nuevo","ID: "+idPersonaje);
            datos.getDb().setTransactionSuccessful();
        } finally {
            datos.getDb().endTransaction();
        }
    }

    public class transaccionAsincronaEjemplo extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                datos.getDb().beginTransaction();
                //datos.insertarClase();
                //datos.insertarRaza();
                datos.getDb().setTransactionSuccessful();
            } finally {
                datos.getDb().endTransaction();
            }
            return null;
        }
    }

}
