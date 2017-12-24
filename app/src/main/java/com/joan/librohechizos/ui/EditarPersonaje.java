package com.joan.librohechizos.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.joan.librohechizos.Interfazes.Listable;
import com.joan.librohechizos.R;
import com.joan.librohechizos.modelo.Clase;
import com.joan.librohechizos.modelo.Personaje;
import com.joan.librohechizos.modelo.Raza;
import com.joan.librohechizos.utiles.AdaptadorSpinner;

import java.util.LinkedList;

/**
 * Created by Joan on 26/03/2017.
 */

public class EditarPersonaje extends AppCompatActivity {
    private Personaje pj;
    private EditText edtNombre;
    private Spinner spnClase;
    private Spinner spnRaza;
    private LinkedList<Listable> listaRaza;
    private LinkedList<Listable> listaClase;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_crear_personaje);

        this.pj = obtenerPersonaje(getIntent().getStringExtra("idPersonaje"));

        edtNombre = (EditText) findViewById(R.id.edt_personaje_nombre);
        spnClase = (Spinner) findViewById(R.id.spn_personaje_clase);
        spnRaza = (Spinner) findViewById(R.id.spn_personaje_raza);

        listaClase=new LinkedList<>();
        listaRaza=new LinkedList<>();

        spnClase.setAdapter(cargarClases());
        spnRaza.setAdapter(cargarRazas());
        edtNombre.setText(pj.getNombre());

        //Estado inicial de los spinners
        posicionIncialSpinner(listaClase,this.pj.getClase(),spnClase);
        posicionIncialSpinner(listaRaza,this.pj.getRaza(),spnRaza);
    }

    private void posicionIncialSpinner(LinkedList<Listable> lista,Listable elemento,Spinner spinner){
        int i = 0,pos = 0;
        while (i < lista.size()) {
            if (elemento.equals(lista.get(i))) {
                pos = i;
                i = lista.size() + 1;
            }
            i++;
        }
        spinner.setSelection(pos);
    }

    private ArrayAdapter cargarClases() {
        listaClase.clear();
        listaClase = Clase.getClases(getApplicationContext());
        AdaptadorSpinner adtSpnClases = new AdaptadorSpinner(this, R.layout.spiner_personalizado,listaClase);
        return adtSpnClases;
    }

    private ArrayAdapter cargarRazas() {
        listaRaza.clear();
        listaRaza = Raza.getRazas(this.getApplicationContext());
        AdaptadorSpinner adtSpnRazas = new AdaptadorSpinner(this, R.layout.spiner_personalizado, listaRaza);
        return adtSpnRazas;
    }

    public void btnPersonajeCrear(View v) {
        String nombre,idRaza,idClase;
        nombre = edtNombre.getText().toString();
        idClase = ((Clase) spnClase.getSelectedItem()).getIdClase();
        idRaza = ((Raza) spnRaza.getSelectedItem()).getIdRaza();
        Personaje personaje = new Personaje(pj.getIdPersonaje(),nombre,idClase,idRaza);
        Personaje.editar(personaje,this.getApplicationContext());
        finish();

    }

    public Personaje obtenerPersonaje(String id){
        Personaje pj = new Personaje(id);
        pj = Personaje.obtener(pj,getApplicationContext());
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
