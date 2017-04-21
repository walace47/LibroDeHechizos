package com.joan.librohechizos.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.joan.librohechizos.R;
import com.joan.librohechizos.modelo.Clase;
import com.joan.librohechizos.modelo.Personaje;
import com.joan.librohechizos.modelo.Raza;
import com.joan.librohechizos.sqlite.OperacionesBD;
import com.joan.librohechizos.utiles.AdaptadorSpinner;
import com.joan.librohechizos.utiles.ComunicadorDePersonajes;

import java.util.ArrayList;

/**
 * Created by Joan on 26/03/2017.
 */

public class EditarPersonaje extends AppCompatActivity {
    private Personaje pj;
    private EditText edtNombre;
    private Spinner spnClase;
    private Spinner spnRaza;
    private OperacionesBD datos;
    private ArrayList<Object> listaRaza;
    private ArrayList<Object> listaClase;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_crear_personaje);
        int posClase=0,posRaza=0;
        datos = OperacionesBD.obtenerInstancia(getApplicationContext());
        this.pj = obtenerPersonaje(getIntent().getStringExtra("idPersonaje"));
        edtNombre = (EditText) findViewById(R.id.edt_personaje_nombre);
        spnClase = (Spinner) findViewById(R.id.spn_personaje_clase);
        spnRaza = (Spinner) findViewById(R.id.spn_personaje_raza);
        listaClase=new ArrayList<>();
        listaRaza=new ArrayList<>();
        spnClase.setAdapter(cargarClases());
        spnRaza.setAdapter(cargarRazas());
        edtNombre.setText(pj.getNombre());
        int i = 0;
        Raza raz;
        Clase clas;
        while (i < listaRaza.size()) {
            raz = (Raza) listaRaza.get(i);
            if (raz.getNombre().equals( pj.getIdRaza())) {
                posRaza=i;
                i = listaRaza.size() + 1;
            } else {
                i++;
            }
        }
        spnRaza.setSelection(posRaza);
        i = 0;
        while (i < listaClase.size()) {
            clas = (Clase) listaClase.get(i);
            if (clas.getNombre().equals(pj.getIdClase())) {
                posClase=i;
                i = listaClase.size() + 1;
            } else {
                i++;
            }


        }
        spnClase.setSelection(posClase);
    }

    private ArrayAdapter cargarClases() {
        Cursor listaClases;
        listaClase.clear();
        try {
            datos.getDb().beginTransaction();
            listaClases = datos.obtenerClases();
            while (listaClases.moveToNext()) {
                listaClase.add(new Clase(listaClases.getString(0), listaClases.getString(1)));
            }
            datos.getDb().setTransactionSuccessful();
        } finally {
            datos.getDb().endTransaction();
        }
        AdaptadorSpinner adtSpnClases = new AdaptadorSpinner(this, R.layout.spiner_personalizado,listaClase);
        return adtSpnClases;
    }

    private ArrayAdapter cargarRazas() {
        Cursor listaRazas;
        listaRaza.clear();
        try {
            datos.getDb().beginTransaction();
            listaRazas = datos.obtenerRazas();
            while (listaRazas.moveToNext()) {
                listaRaza.add(new Raza(listaRazas.getString(0), listaRazas.getString(1)));

            }
            datos.getDb().setTransactionSuccessful();
        } finally {
            datos.getDb().endTransaction();
        }
        AdaptadorSpinner adtSpnRazas = new AdaptadorSpinner(this, R.layout.spiner_personalizado, listaRaza);
        return adtSpnRazas;
    }

    public void btnPersonajeCrear(View v) {
        String nombre,idRaza,idClase;
        nombre = edtNombre.getText().toString();
        idClase = ((Clase) spnClase.getSelectedItem()).getIdClase();
        idRaza = ((Raza) spnRaza.getSelectedItem()).getIdRaza();
        try{
            datos.getDb().beginTransaction();
            datos.editarPersonaje(pj.getIdPersonaje(),nombre,idRaza,idClase);
            datos.getDb().setTransactionSuccessful();
        }finally {
            datos.getDb().endTransaction();

        }
        finish();

    }

    public Personaje obtenerPersonaje(String id){
        Personaje pj=null;
        Cursor listaPersonajes = datos.obtenerPersonaje(id);
        try {
             if(listaPersonajes.moveToNext()) {
                pj=new Personaje(listaPersonajes.getString(0),listaPersonajes.getString(1),
                        listaPersonajes.getString(2),listaPersonajes.getString(3));
            }
        }finally {
            listaPersonajes.close();

        }
        return pj;
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
