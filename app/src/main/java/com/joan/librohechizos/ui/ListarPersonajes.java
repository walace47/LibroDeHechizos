package com.joan.librohechizos.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.joan.librohechizos.R;
import com.joan.librohechizos.modelo.Personaje;
import com.joan.librohechizos.sqlite.OperacionesBD;
import com.joan.librohechizos.utiles.AdaptadorPersonaje;

import java.util.ArrayList;
import java.util.LinkedList;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class ListarPersonajes extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {
    private ArrayList<Personaje> lista;
    private OperacionesBD datos;
    private ListView listaPersonajes;
    ListView menu;
    NavigationView navigation;
    DrawerLayout drawer;
    //float firstTouchX;
    //float firstTouchY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.listar_personaje2);
        datos = OperacionesBD.obtenerInstancia(getApplicationContext());
       // final ArrayList<Personaje> lista;
        listaPersonajes = (ListView) findViewById(R.id.list_personajes);
        lista = cargarPersonajes();


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);




        navigation = (NavigationView) findViewById(R.id.nav_view);

        navigation.setItemIconTintList(null);
        navigation.setNavigationItemSelectedListener(this);
        //navigation.setItemIconTintList(null);
        funcionalidadClickPersonaje( );
        funcionalidadLongClickPersonaje();

        //personalizarMenu();


    }

    public void funcionalidadClickPersonaje(){
        listaPersonajes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ListarPersonajes.this, LibroDeHechizos.class);
                intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("idPersonaje",lista.get(i).getIdPersonaje());
                startActivity(intent);
            }

        });
    }

    public void funcionalidadLongClickPersonaje( ){
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
                        Personaje.eliminar(lista.get(i),ListarPersonajes.this);
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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_listar_personajes,menu);
        return true;
    }

    private ArrayList<Personaje> cargarPersonajes(){
        //final ArrayList<Personaje> lista;
        lista = Personaje.obtenerTodos(getApplicationContext());
        listaPersonajes.setAdapter(new AdaptadorPersonaje(ListarPersonajes.this,lista));
        actualizarLista();
        return lista;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case(R.id.action_nuevo):
                Intent intent = new Intent(this, CrearPersonaje.class);
                startActivity(intent);
                actualizarLista();
                break;
            case(R.id.donar):
                intent = new Intent(getBaseContext(), Donacion.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        return true;
    }
    private void actualizarLista(){
        AdaptadorPersonaje adp=(AdaptadorPersonaje) listaPersonajes.getAdapter();
        adp.notifyDataSetChanged();
    }

    protected void onResume() {
        super.onResume();
        cargarPersonajes();

    }



    private void eliminarPersonaje(Personaje pj){
        Personaje.eliminar(pj,getApplicationContext());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case(R.id.menu_dotes):
                intent = new Intent(this, ListaDotes.class);
                intent.putExtra("idPersonaje","-1");
                startActivity(intent);
                break;
            case (R.id.menu_hechizos):
                intent = new Intent(this, LibroDeHechizos.class);
                intent.putExtra("idPersonaje","-1");
                startActivity(intent);
            default:
                break;
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


}
