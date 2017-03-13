package com.joan.librohechizos.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import com.joan.librohechizos.R;
import com.joan.librohechizos.modelo.Clase;
import com.joan.librohechizos.modelo.Escuela;
import com.joan.librohechizos.modelo.Hechizo;
import com.joan.librohechizos.modelo.Personaje;
import com.joan.librohechizos.sqlite.OperacionesBD;
import com.joan.librohechizos.utiles.AdaptadorHechizo;
import com.joan.librohechizos.utiles.ComunicadorDeObjetos;

import java.util.ArrayList;

public class LibroDeHechizos extends AppCompatActivity {
    private Personaje personaje;
    TabHost TbH;
    private OperacionesBD datos;
    ArrayList<Hechizo> listaAprendidos;
    ArrayList<Hechizo> listaPreparados;
    ArrayList<Hechizo> listaTodos;
    ListView vistaTodos,vistaAprendidos,vistaPrepado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.libro_de_hechizos);
        datos = OperacionesBD.obtenerInstancia(getApplicationContext());

        TbH = (TabHost) findViewById(R.id.tab_libro); //llamamos al Tabhost
        TbH.setup();                                                         //lo activamos

        TabHost.TabSpec todos = TbH.newTabSpec("tab1");  //aspectos de cada Tab (pestaña)
        TabHost.TabSpec aprendidos = TbH.newTabSpec("tab2");
        TabHost.TabSpec preparados = TbH.newTabSpec("tab3");

        todos.setIndicator("Todos");    //qué queremos que aparezca en las pestañas
        todos.setContent(R.id.tab_todos_los_hechizos); //definimos el id de cada Tab (pestaña)

        aprendidos.setIndicator("Aprendidos");
        aprendidos.setContent(R.id.tab_aprendidos);

        preparados.setIndicator("Preparados");
        preparados.setContent(R.id.tab_preparados);

        TbH.addTab(todos); //añadimos los tabs ya programados
        TbH.addTab(aprendidos);
        TbH.addTab(preparados);

        this.personaje=(Personaje)ComunicadorDeObjetos.getMensaje();
        listaTodos=new ArrayList<>();
        listaAprendidos=new ArrayList<>();
        listarTodoLosHechizos();
        listarHechizosAprendidos();
        vistaTodos = (ListView) findViewById(R.id.list_hechizos);
        vistaAprendidos=(ListView) findViewById(R.id.list_aprendidos);
        if (!listaTodos.isEmpty()) {
            vistaTodos.setAdapter(new AdaptadorHechizo(this, listaTodos));
        }
        vistaAprendidos.setAdapter(new AdaptadorHechizo(this, listaAprendidos));
        agregarFuncionalidadMostrarHechizoClick(vistaTodos);
        agregarFuncionalidadMostrarHechizoClick(vistaAprendidos);
        agregarFuncionalidadLongClickTodo(vistaTodos);


    }

    private void listarHechizosAprendidos(){
        Cursor listaHechizos = datos.obtenerHechizoAprendido(personaje.getIdPersonaje());
        listaAprendidos.clear();
        try {
            while (listaHechizos != null && listaHechizos.moveToNext()) {
                Cursor listarClases=datos.obtenerClasesDeHechizo(listaHechizos.getString(0));
                ArrayList<Clase> listaClase= new ArrayList<>();
                while(listarClases!=null && listarClases.moveToNext()){
                    listaClase.add(new Clase(listarClases.getString(0),listarClases.getString(1)));
                }
                Cursor escuela= datos.obtenerEscuela(listaHechizos.getString(12));
                Escuela esc=new Escuela("","");
                escuela.moveToNext();
                if (escuela!=null){
                    esc.setIdEscuela(escuela.getString(0));
                    esc.setNombre(escuela.getString(1));
                }

                listaAprendidos.add(new Hechizo(listaHechizos.getString(0), listaHechizos.getString(1), listaHechizos.getString(2), listaHechizos.getString(3), listaHechizos.getInt(4), listaHechizos.getInt(5), listaHechizos.getInt(6), listaHechizos.getInt(7), listaHechizos.getString(8), listaHechizos.getInt(9), listaHechizos.getInt(10), listaHechizos.getString(11), esc, listaHechizos.getInt(13), listaHechizos.getString(14),listaClase));
                listarClases.close();
                escuela.close();
            }
        } finally {
            listaHechizos.close();


        }

    }

    private void agregarFuncionalidadLongClickTodo(ListView vista){
        vista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                final int posicion=i;

                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(LibroDeHechizos.this);
                dialogo1.setTitle("Agregar Hechizo");
                dialogo1.setMessage("¿ Desea agregar el hechizo ?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Aprender", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        AdaptadorHechizo adapter=(AdaptadorHechizo) vistaTodos.getAdapter();
                        datos.aprenderHechizo(adapter.getListaActual().get(i).getIdHechizo(),personaje.getIdPersonaje());
                        listarHechizosAprendidos();
                        adapter=(AdaptadorHechizo) vistaAprendidos.getAdapter();
                        adapter.notifyDataSetChanged();

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

    private void agregarFuncionalidadMostrarHechizoClick(ListView vista){
        vista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ComunicadorDeObjetos.setMensaje(listaTodos.get(i));
                Intent intent = new Intent(LibroDeHechizos.this,MostrarHechizo.class);
                startActivity(intent);

            }

        });
    }


    public void listarTodoLosHechizos() {
        Cursor listaHechizos = datos.obtenerHechizos();
        listaTodos.clear();
        try {
            while (listaHechizos != null && listaHechizos.moveToNext()) {
                Cursor listarClases=datos.obtenerClasesDeHechizo(listaHechizos.getString(0));
                ArrayList<Clase> listaClase= new ArrayList<>();
                while(listarClases!=null && listarClases.moveToNext()){
                    listaClase.add(new Clase(listarClases.getString(0),listarClases.getString(1)));
                }
                Cursor escuela= datos.obtenerEscuela(listaHechizos.getString(12));
                Escuela esc=new Escuela("","");
                escuela.moveToNext();
                if (escuela!=null){
                    esc.setIdEscuela(escuela.getString(0));
                    esc.setNombre(escuela.getString(1));
                }

                listaTodos.add(new Hechizo(listaHechizos.getString(0), listaHechizos.getString(1), listaHechizos.getString(2), listaHechizos.getString(3), listaHechizos.getInt(4), listaHechizos.getInt(5), listaHechizos.getInt(6), listaHechizos.getInt(7), listaHechizos.getString(8), listaHechizos.getInt(9), listaHechizos.getInt(10), listaHechizos.getString(11), esc, listaHechizos.getInt(13), listaHechizos.getString(14),listaClase));
                listarClases.close();
                escuela.close();
            }
        } finally {
            listaHechizos.close();

        }
    }

}
