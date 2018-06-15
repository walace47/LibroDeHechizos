package com.joan.librohechizos.ui;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.joan.librohechizos.Interfaces.Listable;
import com.joan.librohechizos.R;
import com.joan.librohechizos.modelo.*;
import com.joan.librohechizos.utiles.AdaptadorSpinner;

import java.util.LinkedList;

public class CrearPersonaje extends AppCompatActivity {
    private EditText edtNombre;
    private Spinner spnClase;
    private Spinner spnRaza;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_crear_personaje);
        edtNombre = (EditText) findViewById(R.id.edt_personaje_nombre);
        spnClase = (Spinner) findViewById(R.id.spn_personaje_clase);
        spnRaza = (Spinner) findViewById(R.id.spn_personaje_raza);
        spnClase.setAdapter(cargarClases());
        spnRaza.setAdapter(cargarRazas());

    }

    private ArrayAdapter cargarClases() {
        Cursor listaClases;
        LinkedList<Listable> clases =  Clase.getClases(getApplicationContext());
        AdaptadorSpinner adtSpnClases = new AdaptadorSpinner(this, R.layout.spiner_personalizado, clases);
        return adtSpnClases;
    }

    private ArrayAdapter cargarRazas() {
        Cursor listaRazas;
        LinkedList<Listable> razas = Raza.getRazas(getApplicationContext());
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
        Personaje personajeNuevo = new Personaje("", nombre, idClase, idRaza);
        boolean exito = Personaje.insertar(personajeNuevo,getApplicationContext());
        Toast mensajeExito;
        if (exito){
            mensajeExito = Toast.makeText(getApplicationContext(),"Personaje creado correctamente",Toast.LENGTH_SHORT);
        }else{
            mensajeExito = Toast.makeText(getApplicationContext(),"Puede que halla un problema y no se a creado correctamente el personaje",Toast.LENGTH_SHORT);

        }
        mensajeExito.show();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //hago un case por si en un futuro agrego mas opciones
                Log.i("ActionBar", "Atrás!");
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
