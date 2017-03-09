package com.joan.librohechizos.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.joan.librohechizos.sqlite.OperacionesBD;

/**
 * Created by Giuliano on 06/03/2017.
 */

public class ListarPersonajesActivity extends AppCompatActivity {

    private OperacionesBD datos;
    private TableLayout tblPersonajes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_listar_personajes);
        Intent intent = new Intent(this, ListarPersonajes.class);
        startActivity(intent);
        //Esto hace que la base de datos se borre cada vez que ejecutas la app
        getApplicationContext().deleteDatabase("librohechizos.sqlite");
        datos = OperacionesBD.obtenerInstancia(getApplicationContext());

        //tblPersonajes = (TableLayout) findViewById(R.id.tbl_personajes);
       // listarPersonajes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        listarPersonajes();
    }

    private void listarPersonajes() {
        tblPersonajes.removeAllViews();
        Cursor listaPersonajes = datos.obtenerPersonajes();
        try {
            while (listaPersonajes.moveToNext()) {
                Button btnPersonaje = new Button(getBaseContext());
                btnPersonaje.setText(
                        listaPersonajes.getString(1) + "\n" +
                                listaPersonajes.getString(2) + "\n" +
                                listaPersonajes.getString(3)
                );
                btnPersonaje.setBackgroundColor(Color.WHITE);
                btnPersonaje.setTextColor(Color.BLACK);

                TableLayout.LayoutParams parametrosFila = new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1f);
                TableRow filaNueva = new TableRow(getBaseContext());
                filaNueva.setGravity(1);
                filaNueva.setPadding(0, 0, 0, 20);
                filaNueva.addView(btnPersonaje);
                filaNueva.setLayoutParams(parametrosFila);

                tblPersonajes.addView(filaNueva);
            }
        } finally {
            listaPersonajes.close();
        }
    }

    public void btnCrearPersonaje(View view) {
        Intent intent = new Intent(this, CrearPersonajeActivity.class);
        startActivity(intent);
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

    private void consultasEjemplos() {
        //Consulta todas las clases y las muestra por la consola (no muestra en la app)
        Log.d("Clases", "Lista de clases");
        DatabaseUtils.dumpCursor(datos.obtenerClases());

        //Consulta todas las razas y las muestra por la consola (no muestra en la app)
        Log.d("Razas", "Lista de razas");
        DatabaseUtils.dumpCursor(datos.obtenerRazas());

        //Consulta todos los personajes y los muestra por la consola (no muestra en la app)
        Log.d("Personajes", "Lista de personajes");
        DatabaseUtils.dumpCursor(datos.obtenerPersonajes());
    }

}
