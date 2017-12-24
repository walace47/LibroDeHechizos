package com.joan.librohechizos.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.joan.librohechizos.R;
import com.joan.librohechizos.modelo.Personaje;
import com.joan.librohechizos.sqlite.OperacionesBD;
import com.joan.librohechizos.utiles.AdaptadorPersonaje;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class ListarPersonajes extends AppCompatActivity {
    private ArrayList<Personaje> lista;
    private OperacionesBD datos;
    private ListView listaPersonajes;
    ListView menu;
    DrawerLayout drawerLayout;
    //float firstTouchX;
    //float firstTouchY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.listar_personaje2);
        datos = OperacionesBD.obtenerInstancia(getApplicationContext());
        lista=new ArrayList<>();
        listaPersonajes = (ListView) findViewById(R.id.list_personajes);
        listaPersonajes.setAdapter(new AdaptadorPersonaje(ListarPersonajes.this,lista));
        menu = (ListView) findViewById(R.id.menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        //personalizarMenu();

        cargarPersonajes();
        //metodo que se ejecuta cuando se clikea un personaje
        listaPersonajes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ListarPersonajes.this, LibroDeHechizos.class);
                intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("idPersonaje",lista.get(i).getIdPersonaje());
                   startActivity(intent);
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





    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_listar_personajes,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case(R.id.action_nuevo):
                Intent intent = new Intent(this, CrearPersonaje.class);
                startActivity(intent);
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



    private void cargarPersonajes(){
        obtenerPersonajes();
        AdaptadorPersonaje adp=(AdaptadorPersonaje) listaPersonajes.getAdapter();
        adp.notifyDataSetChanged();

    }


    protected void onResume() {
        super.onResume();
        cargarPersonajes();
    }





    private void obtenerPersonajes(){
        Cursor listaPersonajes = datos.obtenerPersonajes();
        lista.clear();
        try {
            while (listaPersonajes!=null && listaPersonajes.moveToNext()) {
                lista.add(new Personaje(listaPersonajes.getString(0),listaPersonajes.getString(1),
                        listaPersonajes.getString(2),listaPersonajes.getString(3)));
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

  /*
   Por si implemento una barra al costado
   public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //Aqui guardas en una variable privada de clase las coordenadas del primer toque:
                firstTouchX = event.getX();
                firstTouchY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //Aqui ya podemos determinar los tipos de movimientos:
                if(firstTouchX > event.getX()){
                    drawerLayout.closeDrawers();
                }else{
                    drawerLayout.openDrawer(menu);
                }

                break;
        }
        return true;
    }*/
}
