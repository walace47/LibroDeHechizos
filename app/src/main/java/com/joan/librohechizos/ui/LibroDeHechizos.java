package com.joan.librohechizos.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;

import com.joan.librohechizos.R;
import com.joan.librohechizos.modelo.Clase;
import com.joan.librohechizos.modelo.Escuela;
import com.joan.librohechizos.modelo.Hechizo;
import com.joan.librohechizos.modelo.Personaje;
import com.joan.librohechizos.sqlite.OperacionesBD;
import com.joan.librohechizos.utiles.AdaptadorHechizo;
import com.joan.librohechizos.utiles.ComunicadorDeHechizo;
import com.joan.librohechizos.utiles.ComunicadorDePersonajes;
import com.joan.librohechizos.utiles.FiltroHechizo;

import java.util.ArrayList;

public class LibroDeHechizos extends AppCompatActivity {
    private Personaje personaje;
    private TabHost TbH;
    private OperacionesBD datos;
    private ArrayList<Hechizo> listaAprendidos, listaPreparados, listaTodos;
    private String filtro;
    private Button btnFiltro;
    private FiltroHechizo popup;
    private boolean clickFiltro;
    private ListView vistaTodos, vistaAprendidos, vistaPreparado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.libro_de_hechizos);
        clickFiltro=false;
        datos = OperacionesBD.obtenerInstancia(getApplicationContext());
        filtro = "";
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

        //cargo boton filtro
        btnFiltro = (Button) findViewById(R.id.btn_filtrar);
        popup = new FiltroHechizo(this);
        //funcionalidad del boton
        clickBtnFiltrar();

        //las 3 vistas de las listas
        vistaTodos = (ListView) findViewById(R.id.list_hechizos);
        vistaAprendidos = (ListView) findViewById(R.id.list_aprendidos);
        vistaPreparado = (ListView) findViewById(R.id.list_preparados);

        //el personaje de libro de hechizos
        this.personaje = (Personaje)  ComunicadorDePersonajes.getMensaje();

        //Creacion de listas de Hechizos
        listaTodos = new ArrayList<>();
        listaAprendidos = new ArrayList<>();
        listaPreparados = new ArrayList<>();
        listarTodoLosHechizos();
        listarHechizosAprendidos();
        listarHechizosPreparados();

        //seteando adaptadores de la vista
        vistaTodos.setAdapter(new AdaptadorHechizo(this, listaTodos));
        vistaAprendidos.setAdapter(new AdaptadorHechizo(this, listaAprendidos));
        vistaPreparado.setAdapter(new AdaptadorHechizo(this, listaPreparados));

        //agregando funcionalidad cuando se clikean los items
        agregarFuncionalidadMostrarHechizoClick(vistaTodos);
        agregarFuncionalidadMostrarHechizoClick(vistaAprendidos);
        agregarFuncionalidadMostrarHechizoClick(vistaPreparado);
        agregarFuncionalidadLongClickTodo();
        agregarFuncionalidadLongclikAprendido();


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_libro_de_hechizos,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId()==R.id.action_edit){
            Intent intent = new Intent(this, editarPersonaje.class);
            startActivity(intent);
        }
        return true;
    }


    private void agregarFuncionalidadLongClickTodo() {
        vistaTodos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                final int posicion = i;

                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(LibroDeHechizos.this);
                dialogo1.setTitle("Agregar Hechizo");
                dialogo1.setMessage("¿ Desea agregar el hechizo ?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Aprender", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        AdaptadorHechizo adapter = (AdaptadorHechizo) vistaTodos.getAdapter();
                        String idHechizo = adapter.getListaActual().get(i).getIdHechizo();
                        if (!datos.estaAprendido(personaje.getIdPersonaje(), idHechizo)) {
                            try {
                                datos.getDb().beginTransaction();
                                datos.insertarHechizoAprendido(idHechizo, personaje.getIdPersonaje());
                                datos.getDb().setTransactionSuccessful();


                            } finally {
                                datos.getDb().endTransaction();
                            }
                            listarHechizosAprendidos();
                            adapter = (AdaptadorHechizo) vistaAprendidos.getAdapter();
                            adapter.notifyDataSetChanged();

                        }
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

    private void agregarFuncionalidadLongclikAprendido() {
        vistaAprendidos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                final int posicion = i;

                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(LibroDeHechizos.this);
                dialogo1.setTitle("Preparar o Eliminar Hechizo");
                dialogo1.setMessage("¿ Que desea hacer con el hechizo ?");
                //dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Preparar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        AdaptadorHechizo adapter = (AdaptadorHechizo) vistaAprendidos.getAdapter();
                        try {
                            datos.getDb().beginTransaction();
                            datos.prepararHechizo(personaje.getIdPersonaje(), adapter.getListaActual().get(i).getIdHechizo());
                            datos.getDb().setTransactionSuccessful();
                        } finally {
                            datos.getDb().endTransaction();
                            listarHechizosPreparados();
                            listarHechizosAprendidos();
                            adapter.notifyDataSetChanged();
                            adapter = (AdaptadorHechizo) vistaPreparado.getAdapter();
                            adapter.notifyDataSetChanged();

                        }


                    }
                });
                dialogo1.setNegativeButton("Eliminar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        AdaptadorHechizo adapter = (AdaptadorHechizo) vistaAprendidos.getAdapter();
                        try {
                            datos.getDb().beginTransaction();
                            datos.eliminarHechizoAprendido(personaje.getIdPersonaje(), adapter.getListaActual().get(i).getIdHechizo());
                            datos.getDb().setTransactionSuccessful();
                        } finally {
                            datos.getDb().endTransaction();
                            listarHechizosPreparados();
                            listarHechizosAprendidos();
                            adapter.notifyDataSetChanged();
                            adapter = (AdaptadorHechizo) vistaPreparado.getAdapter();
                            adapter.notifyDataSetChanged();

                        }


                    }
                });
                dialogo1.show();


                return true;
            }
        });
    }

    private void agregarFuncionalidadMostrarHechizoClick(final ListView vista) {
        vista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (vista.equals(vistaTodos)){
                    ComunicadorDeHechizo.setMensaje(listaTodos.get(i));
                }else{
                    if (vista.equals(vistaAprendidos)) {
                        ComunicadorDeHechizo.setMensaje(listaAprendidos.get(i));

                    }else{
                        if(vista.equals(vistaPreparado)){
                            ComunicadorDeHechizo.setMensaje(listaPreparados.get(i));

                        }
                    }
                }

                Intent intent = new Intent(LibroDeHechizos.this, MostrarHechizo.class);
                startActivity(intent);

            }

        });
    }

    public void listarHechizosAprendidos() {
        listaAprendidos.clear();
        Cursor listaHechizos = datos.obtenerHechizoAprendido(personaje.getIdPersonaje(), filtro);
        try {
            while (listaHechizos != null && listaHechizos.moveToNext()) {
                Cursor listarClases = datos.obtenerClasesDeHechizo(listaHechizos.getString(0));
                ArrayList<Clase> listaClase = new ArrayList<>();
                while (listarClases != null && listarClases.moveToNext()) {
                    listaClase.add(new Clase(listarClases.getString(0), listarClases.getString(1)));
                }
                Cursor escuela = datos.obtenerEscuela(listaHechizos.getString(12));
                Escuela esc = new Escuela("", "");
                if (escuela.moveToNext()) {
                    esc.setIdEscuela(escuela.getString(0));
                    esc.setNombre(escuela.getString(1));
                }

                listaAprendidos.add(new Hechizo(listaHechizos.getString(0), listaHechizos.getString(1),
                        listaHechizos.getString(2), listaHechizos.getString(3), listaHechizos.getInt(4),
                        listaHechizos.getInt(5), listaHechizos.getInt(6), listaHechizos.getInt(7), listaHechizos.getString(8),
                        listaHechizos.getInt(9), listaHechizos.getInt(10), listaHechizos.getString(11), esc, listaHechizos.getInt(13),
                        listaHechizos.getString(14), listaClase));
                listarClases.close();
                escuela.close();
            }
        } finally {
            listaHechizos.close();


        }

    }

    public void listarHechizosPreparados() {
        listaPreparados.clear();
        Cursor listaHechizos = datos.obtenerHechizoPreparado(personaje.getIdPersonaje(), filtro);
        try {
            while (listaHechizos != null && listaHechizos.moveToNext()) {
                //obtenga las clases que pueden aprender este hechizo
                Cursor listarClases = datos.obtenerClasesDeHechizo(listaHechizos.getString(0));
                ArrayList<Clase> listaClase = new ArrayList<>();
                while (listarClases != null && listarClases.moveToNext()) {
                    listaClase.add(new Clase(listarClases.getString(0), listarClases.getString(1)));
                }
                //obtengo la escuela del hechizo
                Cursor escuela = datos.obtenerEscuela(listaHechizos.getString(12));
                Escuela esc = new Escuela("", "");
                if (escuela.moveToNext()) {
                    esc.setIdEscuela(escuela.getString(0));
                    esc.setNombre(escuela.getString(1));
                }
                // obtengo el resto de los datos
                listaPreparados.add(new Hechizo(listaHechizos.getString(0), listaHechizos.getString(1),
                        listaHechizos.getString(2), listaHechizos.getString(3), listaHechizos.getInt(4),
                        listaHechizos.getInt(5), listaHechizos.getInt(6), listaHechizos.getInt(7), listaHechizos.getString(8),
                        listaHechizos.getInt(9), listaHechizos.getInt(10), listaHechizos.getString(11), esc, listaHechizos.getInt(13),
                        listaHechizos.getString(14), listaClase));
                listarClases.close();
                escuela.close();
            }
        } finally {
            listaHechizos.close();


        }
    }

    public void listarTodoLosHechizos() {
        Cursor listaHechizos = datos.obtenerHechizos(filtro);
        listaTodos.clear();
        try {
            while (listaHechizos != null && listaHechizos.moveToNext()) {
                Cursor listarClases = datos.obtenerClasesDeHechizo(listaHechizos.getString(0));
                ArrayList<Clase> listaClase = new ArrayList<>();
                while (listarClases != null && listarClases.moveToNext()) {
                    listaClase.add(new Clase(listarClases.getString(0), listarClases.getString(1)));
                }
                Cursor escuela = datos.obtenerEscuela(listaHechizos.getString(12));
                Escuela esc = new Escuela("", "");
                if (escuela.moveToNext()) {
                    esc.setIdEscuela(escuela.getString(0));
                    esc.setNombre(escuela.getString(1));
                }

                listaTodos.add(new Hechizo(listaHechizos.getString(0), listaHechizos.getString(1), listaHechizos.getString(2), listaHechizos.getString(3), listaHechizos.getInt(4), listaHechizos.getInt(5), listaHechizos.getInt(6), listaHechizos.getInt(7), listaHechizos.getString(8), listaHechizos.getInt(9), listaHechizos.getInt(10), listaHechizos.getString(11), esc, listaHechizos.getInt(13), listaHechizos.getString(14), listaClase));
                listarClases.close();
                escuela.close();
            }
        } finally {
            listaHechizos.close();

        }
    }

    private void clickBtnFiltrar() {
        btnFiltro.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(clickFiltro) {
                    clickFiltro=false;
                    popup.getPopupWindow().dismiss();
                }else{
                    clickFiltro=true;
                    popup.getPopupWindow().showAsDropDown(btnFiltro, 50, 0);

                }
            }
        });


    }



    public void setFiltro(String nuevoFiltro) {
        this.filtro = nuevoFiltro;
        listarTodoLosHechizos();
        listarHechizosAprendidos();
        listarHechizosPreparados();
        AdaptadorHechizo adapter = (AdaptadorHechizo) vistaAprendidos.getAdapter();
        adapter.notifyDataSetChanged();
        adapter = (AdaptadorHechizo) vistaPreparado.getAdapter();
        adapter.notifyDataSetChanged();
        adapter = (AdaptadorHechizo) vistaTodos.getAdapter();
        adapter.notifyDataSetChanged();

    }

    public void setClickFiltro(boolean clickFiltro) {
        this.clickFiltro = clickFiltro;
    }
}
