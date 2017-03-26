package com.joan.librohechizos.ui;

import android.database.Cursor;
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
import com.joan.librohechizos.utiles.AdaptadorSpinner;

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
        ArrayList<Object> clases = new ArrayList<>();
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
        AdaptadorSpinner adtSpnClases = new AdaptadorSpinner(this, R.layout.spiner_personalizado, clases);
        return adtSpnClases;
    }

    private ArrayAdapter cargarRazas() {
        Cursor listaRazas;
        ArrayList<Object> razas = new ArrayList<>();
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
        AdaptadorSpinner adtSpnRazas = new AdaptadorSpinner(this,  R.layout.spiner_personalizado, razas);
        return adtSpnRazas;
    }

    public void btnPersonajeCrear(View v) {
        if (!edtNombre.getText().toString().equals("")) {
            String nombre = edtNombre.getText().toString();
            String idClase = ((Clase) spnClase.getSelectedItem()).getIdClase();
            String idRaza = ((Raza) spnRaza.getSelectedItem()).getIdRaza();

            insertarPersonaje(nombre,idClase,idRaza);
            //Esto cierra el activity de CrearPersonaje, y por lo tanto vuelve al activity anterior
            //que es el ListarPersonajes
            finish();
        }
    }

    private void insertarPersonaje(String nombre,String idClase,String idRaza){
        long idPersonaje;
        try {
            datos.getDb().beginTransaction();
            Personaje personajeNuevo = new Personaje("", nombre, idClase, idRaza);

            idPersonaje = datos.insertarPersonaje(personajeNuevo);
            Log.d("Personaj0e nuevo", "ID: " + idPersonaje);

            datos.getDb().setTransactionSuccessful();
            Toast mensajeExito = Toast.makeText(getApplicationContext(),"Personaje creado correctamente",Toast.LENGTH_SHORT);
            mensajeExito.show();

        } finally {
            datos.getDb().endTransaction();
        }


    }

}
