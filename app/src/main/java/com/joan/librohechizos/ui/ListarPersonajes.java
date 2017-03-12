package com.joan.librohechizos.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.joan.librohechizos.utiles.AdaptadorPersonaje;

import java.util.ArrayList;

public class ListarPersonajes extends AppCompatActivity {
    private ArrayList<Personaje> lista;
    private OperacionesBD datos;
    private ListView listaPersonajes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listar_personaje);
        getApplicationContext().deleteDatabase("librohechizos.sqlite");
        datos = OperacionesBD.obtenerInstancia(getApplicationContext());
        lista=new ArrayList<Personaje>();
        listaPersonajes = (ListView) findViewById(R.id.list_personajes);
        listaPersonajes.setAdapter(new AdaptadorPersonaje(ListarPersonajes.this,lista));

        cargarPersonajes();

        //metodo que se ejecuta cuando se clikea un personaje
        listaPersonajes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ListarPersonajes.this, LibroDeHechizos.class);
                startActivity(intent);
                //Toast.makeText(ListarPersonajes.this,lista.get(i).getNombre(), Toast.LENGTH_LONG).show();


            }

        });
        //cuando se quiere eliminar un personaje
        listaPersonajes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final int posicion=i;

                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(ListarPersonajes.this);
                dialogo1.setTitle("Importante");
                dialogo1.setMessage("¿ Elimina el personaje ?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        eliminarPersonaje(lista.get(i).getIdPersonaje());
                        lista.remove(posicion);
                        cargarPersonajes();
                    }
                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                    }
                });
                dialogo1.show();

                return true;
            }
        });


    }



    private void cargarPersonajes(){
        obtenerPersonajes();
        AdaptadorPersonaje adp=(AdaptadorPersonaje) listaPersonajes.getAdapter();
        adp.notifyDataSetChanged();
        //listaPersonajes.setAdapter(adaptador);

    }


    protected void onResume() {
        super.onResume();
        cargarPersonajes();
    }

    public void btnCrearPersonaje(View view) {
        //Intent intent = new Intent(this, MostrarHechizo.class);
        //startActivity(intent);
        Intent intent = new Intent(this, CrearPersonajeActivity.class);
        startActivity(intent);
    }

    public void mostrarNombre(View v){
        Toast.makeText(this, "hola",
                Toast.LENGTH_SHORT).show();
    }



    private void obtenerPersonajes(){
        Cursor listaPersonajes = datos.obtenerPersonajes();
        lista.clear();
        try {
            while (listaPersonajes!=null && listaPersonajes.moveToNext()) {
                lista.add(new Personaje(listaPersonajes.getString(0),listaPersonajes.getString(1),listaPersonajes.getString(2),listaPersonajes.getString(3)));
            }
        }finally {
            listaPersonajes.close();

        }
    }

    private void eliminarPersonaje(String idPersonaje){
        try {
            datos.getDb().beginTransaction();
            datos.eliminarPersonaje(idPersonaje);
            datos.getDb().setTransactionSuccessful();
            Toast mensajeExito = Toast.makeText(getApplicationContext(),"Personaje eliminado correctamente",Toast.LENGTH_SHORT);
            mensajeExito.show();

        } finally {
            datos.getDb().endTransaction();
        }


    }
}
