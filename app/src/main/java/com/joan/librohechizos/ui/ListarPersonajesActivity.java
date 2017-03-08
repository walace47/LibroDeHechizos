package com.joan.librohechizos.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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

        TableLayout tblPersonajes = (TableLayout) findViewById(R.id.tbl_personajes);

        Cursor listaPersonajes = datos.obtenerPersonajes();

        try {
            while (listaPersonajes.moveToNext()) {
                TableRow filaNueva = new TableRow(getBaseContext());
                TableLayout.LayoutParams parametros = new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.MATCH_PARENT);

                Button btnPersonaje = new Button(getBaseContext());
                btnPersonaje.setText(
                        listaPersonajes.getString(1)+"\n"+
                        listaPersonajes.getString(2)+"\n"+
                        listaPersonajes.getString(3)
                );
                btnPersonaje.setTextSize(20);
                btnPersonaje.setTextColor(Color.BLACK);

                filaNueva.setLayoutParams(parametros);
                filaNueva.setGravity(1);
                filaNueva.setPadding(0,0,0,20);
                filaNueva.addView(btnPersonaje);

                tblPersonajes.addView(filaNueva);
            }
        } finally {
            listaPersonajes.close();
        }

        /*
        //Consulta todas las clases y las muestra por la consola (no muestra en la app)
        Log.d("Clases","Lista de clases");
        DatabaseUtils.dumpCursor(datos.obtenerClases());

        //Consulta todas las razas y las muestra por la consola (no muestra en la app)
        Log.d("Razas","Lista de razas");
        DatabaseUtils.dumpCursor(datos.obtenerRazas());

        //Consulta todos los personajes y los muestra por la consola (no muestra en la app)
        Log.d("Personajes","Lista de personajes");
        DatabaseUtils.dumpCursor(datos.obtenerPersonajes());
        */
    }

    public void btnCrearPersonaje(View view) {
        Intent intent = new Intent(ListarPersonajesActivity.this, CrearPersonajeActivity.class);
        startActivity(intent);
    }

    private void precargarDatos() {
        try {
            datos.getDb().beginTransaction();
            long idClase = datos.insertarClase(new Clase("","Guerrero"));
            Log.d("Clase nueva","ID: "+idClase);
            idClase = datos.insertarClase(new Clase("","Paladin"));
            Log.d("Clase nueva","ID: "+idClase);
            idClase = datos.insertarClase(new Clase("","Clerigo"));
            Log.d("Clase nueva","ID: "+idClase);
            idClase = datos.insertarClase(new Clase("","Druida"));
            Log.d("Clase nueva","ID: "+idClase);
            idClase = datos.insertarClase(new Clase("","Hechizero"));
            Log.d("Clase nueva","ID: "+idClase);
            idClase = datos.insertarClase(new Clase("","Mago"));
            Log.d("Clase nueva","ID: "+idClase);
            idClase = datos.insertarClase(new Clase("","Brujo"));
            Log.d("Clase nueva","ID: "+idClase);
            //se cargaron las clases
            long idRaza = datos.insertarRaza(new Raza("","Humano"));
            Log.d("Raza nueva","ID: "+idRaza);
            idRaza = datos.insertarRaza(new Raza("","Draconido"));
            Log.d("Raza nueva","ID: "+idRaza);
            idRaza = datos.insertarRaza(new Raza("","Elfo"));
            Log.d("Raza nueva","ID: "+idRaza);
            idRaza = datos.insertarRaza(new Raza("","Tieflin"));
            Log.d("Raza nueva","ID: "+idRaza);
            idRaza = datos.insertarRaza(new Raza("","Gnomo"));
            Log.d("Raza nueva","ID: "+idRaza);
            idRaza = datos.insertarRaza(new Raza("","Mediano"));
            Log.d("Raza nueva","ID: "+idRaza);
            idRaza = datos.insertarRaza(new Raza("","Enano"));
            Log.d("Raza nueva","ID: "+idRaza);
            idRaza = datos.insertarRaza(new Raza("","Genazi"));
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
                //datos.consulta...
                datos.getDb().setTransactionSuccessful();
            } finally {
                datos.getDb().endTransaction();
            }
            return null;
        }
    }

}
