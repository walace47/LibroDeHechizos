package com.joan.librohechizos.ui;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.joan.librohechizos.R;
import com.joan.librohechizos.modelo.Clase;
import com.joan.librohechizos.modelo.Personaje;
import com.joan.librohechizos.modelo.Raza;
import com.joan.librohechizos.sqlite.OperacionesBD;

import java.util.ArrayList;

public class CrearPersonajeActivity extends AppCompatActivity {
    private EditText nombre;
    private Spinner clase;
    private Spinner raza;
    private OperacionesBD datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_personaje);
        datos = OperacionesBD.obtenerInstancia(getApplicationContext());
        nombre = (EditText) findViewById(R.id.edt_personaje_nombre);
        clase = (Spinner) findViewById(R.id.spn_personaje_clase);
        raza = (Spinner) findViewById(R.id.spn_personaje_raza);
        clase.setAdapter(llenarSpinnerClase());
        raza.setAdapter(llenarSpinnerRaza());
    }

    private ArrayAdapter llenarSpinnerClase() {
        Cursor consulta;
        ArrayList<String> clases = new ArrayList<>();
        try {
            datos.getDb().beginTransaction();
            consulta = datos.obtenerClases();
            while (consulta.moveToNext()) {
                clases.add(consulta.getString(1));
            }
            datos.getDb().setTransactionSuccessful();
        } finally {
            datos.getDb().endTransaction();
        }

        ArrayAdapter<String> lista = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, clases);

        return lista;
    }

    private ArrayAdapter llenarSpinnerRaza() {
        Cursor consulta;
        ArrayList<String> razas = new ArrayList<>();
        try {
            datos.getDb().beginTransaction();
            consulta = datos.obtenerRazas();
            while (consulta.moveToNext()) {
                razas.add(consulta.getString(1));
            }
            datos.getDb().setTransactionSuccessful();
        } finally {
            datos.getDb().endTransaction();
        }

        ArrayAdapter<String> lista = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, razas);

        return lista;
    }

    public void btnPersonajeCrear(View v) {
        try {
            datos.getDb().beginTransaction();
            String nom,raz,clas;
            Cursor cursorAux;
            cursorAux=datos.obtenerClases(clase.getSelectedItem().toString());
            clas =cursorAux.getString(0);
            nom = nombre.getText().toString();
            cursorAux=datos.obtenerRazas(raza.getSelectedItem().toString());
            raz=cursorAux.getString(0);
            Personaje pj=new Personaje("",nom,clas,raz);
            long idPersonaje=datos.insertarPersonaje(pj);
            Log.d("Personaje nuevo","ID: "+idPersonaje);
            datos.getDb().setTransactionSuccessful();
        }finally {
            datos.getDb().endTransaction();
            Intent intent = new Intent(CrearPersonajeActivity.this, ListarPersonajesActivity.class);
            startActivity(intent);
        }
    }

}
