package com.joan.librohechizos.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.joan.librohechizos.R;
import com.joan.librohechizos.modelo.*;
import com.joan.librohechizos.sqlite.OperacionesBD;

import java.util.ArrayList;

public class CrearPersonajeActivity extends AppCompatActivity {
    private EditText edtNombre;
    private Spinner spnClase;
    private Spinner spnRaza;
    private OperacionesBD datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_personaje);
        datos = OperacionesBD.obtenerInstancia(getApplicationContext());

        edtNombre = (EditText) findViewById(R.id.edt_personaje_nombre);
        spnClase = (Spinner) findViewById(R.id.spn_personaje_clase);
        spnRaza = (Spinner) findViewById(R.id.spn_personaje_raza);

        spnClase.setAdapter(cargarClases());
        spnRaza.setAdapter(cargarRazas());

    }

    private ArrayAdapter cargarClases() {
        Cursor listaClases;
        ArrayList<Clase> clases = new ArrayList<>();
        try {
            datos.getDb().beginTransaction();
            listaClases = datos.obtenerClases();
            while (listaClases.moveToNext()) {
                clases.add(new Clase(listaClases.getString(0), listaClases.getString(1)));
            }
            datos.getDb().setTransactionSuccessful();
        } finally {
            datos.getDb().endTransaction();
        }
        ArrayAdapter<Clase> adtSpnClases = new ArrayAdapter<Clase>(this, android.R.layout.simple_spinner_item, clases);
        return adtSpnClases;
    }

    private ArrayAdapter cargarRazas() {
        Cursor listaRazas;
        ArrayList<Raza> razas = new ArrayList<>();
        try {
            datos.getDb().beginTransaction();
            listaRazas = datos.obtenerRazas();
            while (listaRazas.moveToNext()) {
                razas.add(new Raza(listaRazas.getString(0), listaRazas.getString(1)));
            }
            datos.getDb().setTransactionSuccessful();
        } finally {
            datos.getDb().endTransaction();
        }
        ArrayAdapter<Raza> adtSpnRazas = new ArrayAdapter<Raza>(this, android.R.layout.simple_spinner_item, razas);
        return adtSpnRazas;
    }

    public void btnPersonajeCrear(View v) {
        if (!edtNombre.getText().toString().equals("")) {
            String nombre = edtNombre.getText().toString();
            String idClase = ((Clase) spnClase.getSelectedItem()).getIdClase();
            String idRaza = ((Raza) spnRaza.getSelectedItem()).getIdRaza();

            new insertarPersonaje(nombre,idClase,idRaza).execute();
            //Esto cierra el activity de CrearPersonaje, y por lo tanto vuelve al activity anterior
            //que es el ListarPersonajes
            finish();
        }
    }

    public class insertarPersonaje extends AsyncTask<Void, Void, Void> {

        private String nombre;
        private String idClase;
        private String idRaza;

        public insertarPersonaje(String nombre, String idClase, String idRaza) {
            this.nombre = nombre;
            this.idClase = idClase;
            this.idRaza = idRaza;
        }

        @Override
        protected Void doInBackground(Void... params) {
            long idPersonaje = -1;
            try {
                datos.getDb().beginTransaction();

                Personaje personajeNuevo = new Personaje("", nombre, idClase, idRaza);

                idPersonaje = datos.insertarPersonaje(personajeNuevo);
                Log.d("Personaje nuevo", "ID: " + idPersonaje);

                datos.getDb().setTransactionSuccessful();
            } finally {
                datos.getDb().endTransaction();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast mensajeExito = Toast.makeText(getApplicationContext(),"Personaje creado correctamente",Toast.LENGTH_SHORT);
            mensajeExito.show();
        }
    }

}
