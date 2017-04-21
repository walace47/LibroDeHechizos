package com.joan.librohechizos.ui;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.joan.librohechizos.utiles.ComunicadorDeHechizo;
import com.joan.librohechizos.utiles.ComunicadorDePersonajes;
import com.joan.librohechizos.utiles.FiltroHechizo;

import java.util.ArrayList;

public class LibroDeHechizos extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private Personaje personaje;
    private TabHost TbH;
    private OperacionesBD datos;
    private ArrayList<Hechizo> listaAprendidos, listaPreparados, listaTodos;
    private String filtro, textoFiltro;
    private Button btnFiltro;
    private FiltroHechizo popup;
    private boolean clickFiltro;
    private ListView vistaTodos, vistaAprendidos, vistaPreparado;
    private SearchView mSearchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.libro_de_hechizos);
        clickFiltro = false;
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

        textoFiltro = "";
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
        this.personaje = obtenerPersonaje(getIntent().getStringExtra("idPersonaje"));

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

        vistaTodos.setFooterDividersEnabled(true);
        vistaAprendidos.setFooterDividersEnabled(true);
        vistaPreparado.setFooterDividersEnabled(true);


        //agregando funcionalidad cuando se clikean los items
        agregarFuncionalidadMostrarHechizoClick(vistaTodos);
        agregarFuncionalidadMostrarHechizoClick(vistaAprendidos);
        agregarFuncionalidadMostrarHechizoClick(vistaPreparado);
        agregarFuncionalidadLongClickTodo();
        agregarFuncionalidadLongclikAprendido();
        agregarFuncionalidadLongClickPreparados();


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_libro_de_hechizos, menu);
        MenuItem searchItem = menu.findItem(R.id.buscar);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint("Search...");


        return true;
    }

    public Personaje obtenerPersonaje(String id) {
        Personaje pj = null;
        Cursor listaPersonajes = datos.obtenerPersonaje(id);
        try {
            while (listaPersonajes != null && listaPersonajes.moveToNext()) {
                pj = new Personaje(listaPersonajes.getString(0), listaPersonajes.getString(1),
                        listaPersonajes.getString(2), listaPersonajes.getString(3));
            }
        } finally {
            listaPersonajes.close();

        }
        return pj;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.setIntent(intent);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //hago un case por si en un futuro agrego mas opciones
                Log.i("ActionBar", "Atrás!");
                finish();
                return true;
            case R.id.action_edit:
                Intent intent = new Intent(this, EditarPersonaje.class);
                intent.putExtra("idPersonaje", personaje.getIdPersonaje());
                startActivity(intent);
                return (true);
            default:
                return super.onOptionsItemSelected(item);
        }
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
                            adapter.setLista(listaAprendidos);

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

    private void agregarFuncionalidadLongClickPreparados() {
        vistaPreparado.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                final int posicion = i;

                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(LibroDeHechizos.this);
                dialogo1.setTitle("Dejar de preparar");
                dialogo1.setMessage("¿ Desea dejar de prepara el hechizo ?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("dejar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        AdaptadorHechizo adapter = (AdaptadorHechizo) vistaPreparado.getAdapter();
                        String idHechizo = adapter.getListaActual().get(i).getIdHechizo();
                        try {
                            datos.getDb().beginTransaction();
                            datos.dejarDePreparaHechizo(idHechizo, personaje.getIdPersonaje());
                            datos.getDb().setTransactionSuccessful();


                        } finally {
                            datos.getDb().endTransaction();
                        }
                        listarHechizosPreparados();
                        adapter = (AdaptadorHechizo) vistaPreparado.getAdapter();
                        adapter.setLista(listaPreparados);


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
                            adapter.setLista(listaAprendidos);
                            adapter = (AdaptadorHechizo) vistaPreparado.getAdapter();
                            adapter.setLista(listaPreparados);

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
                            adapter.setLista(listaAprendidos);
                            adapter = (AdaptadorHechizo) vistaPreparado.getAdapter();
                            adapter.setLista(listaPreparados);
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
                Hechizo hechizo = (Hechizo) vista.getItemAtPosition(i);
                if (!hechizo.getNombre().equals("-1")) {
                    Intent intent = new Intent(LibroDeHechizos.this, MostrarHechizo.class);
                    intent.putExtra("idHechizo", hechizo.getIdHechizo());
                    startActivity(intent);
                }

            }

        });
    }

    public void listarHechizosAprendidos() {
        listaAprendidos.clear();
        Cursor listarClases;
        Cursor listaHechizos = datos.obtenerHechizoAprendido(personaje.getIdPersonaje(), filtro);
        if (listaHechizos.moveToNext()) {
            listaAprendidos.add(new Hechizo("-1", listaHechizos.getInt(13)));
        }
        listaHechizos.moveToPrevious();
        try {
            while (listaHechizos != null && listaHechizos.moveToNext()) {
                listarClases = datos.obtenerClasesDeHechizo(listaHechizos.getString(0));
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
                if (!listaAprendidos.isEmpty() && listaAprendidos.get(listaAprendidos.size() - 1).getNivel() != listaHechizos.getInt(13)) {
                    listaAprendidos.add(new Hechizo("-1", listaHechizos.getInt(13)));
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
        Cursor listarClases;
        Cursor listaHechizos = datos.obtenerHechizoPreparado(personaje.getIdPersonaje(), filtro);
        if (listaHechizos.moveToNext()) {
            listaPreparados.add(new Hechizo("-1", listaHechizos.getInt(13)));
        }
        listaHechizos.moveToPrevious();
        try {
            while (listaHechizos != null && listaHechizos.moveToNext()) {
                //obtenga las clases que pueden aprender este hechizo
                listarClases = datos.obtenerClasesDeHechizo(listaHechizos.getString(0));
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
                if (!listaPreparados.isEmpty() && listaPreparados.get(listaPreparados.size() - 1).getNivel() != listaHechizos.getInt(13)) {
                    listaPreparados.add(new Hechizo("-1", listaHechizos.getInt(13)));
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
        Cursor listarClases;
        if (listaHechizos.moveToNext()) {
            listaTodos.add(new Hechizo("-1", listaHechizos.getInt(13)));
        }
        listaHechizos.moveToPrevious();
        try {
            while (listaHechizos != null && listaHechizos.moveToNext()) {
                listarClases = datos.obtenerClasesDeHechizo(listaHechizos.getString(0));
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
                if (!listaTodos.isEmpty() && listaTodos.get(listaTodos.size() - 1).getNivel() != listaHechizos.getInt(13)) {
                    listaTodos.add(new Hechizo("-1", listaHechizos.getInt(13)));
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
                if (clickFiltro) {
                    clickFiltro = false;
                    popup.getPopupWindow().dismiss();
                } else {
                    clickFiltro = true;
                    popup.getPopupWindow().showAsDropDown(btnFiltro, 0, 0);

                }
            }
        });


    }

    public void setFiltro(String nuevoFiltro) {
        this.filtro = nuevoFiltro;
        // se cargan las listas con el filtro
        listarTodoLosHechizos();
        listarHechizosAprendidos();
        listarHechizosPreparados();

        AdaptadorHechizo adapter = ((AdaptadorHechizo) vistaAprendidos.getAdapter());
        adapter.setLista(listaAprendidos);
        adapter = (AdaptadorHechizo) vistaPreparado.getAdapter();
        adapter.setLista(listaPreparados);
        adapter = (AdaptadorHechizo) vistaTodos.getAdapter();
        adapter.setLista(listaTodos);
        //coloca el otro filtro
        filtroTexto(textoFiltro);


    }

    public void onDestroy() {
        super.onDestroy();

        if (popup != null && popup.getPopupWindow().isShowing()) {
            popup.getPopupWindow().dismiss();
        }
        finish();
    }

    public void setClickFiltro(boolean clickFiltro) {
        this.clickFiltro = clickFiltro;
    }

    @Override
    public boolean onQueryTextSubmit(String text) {
        Toast.makeText(this, "Searching for " + text, Toast.LENGTH_LONG).show();

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        textoFiltro = newText;
        filtroTexto(textoFiltro);

        return false;
    }

    private void filtroTexto(String txt) {
        ArrayList<Hechizo> sub1, sub2, sub3;
        sub1 = new ArrayList<>();
        sub2 = new ArrayList<>();
        sub3 = new ArrayList<>();

        if (!textoFiltro.equals("")) {

            for (int i = 0; i < listaTodos.size(); i++) {
                if (listaTodos.get(i).getNombre().toLowerCase().indexOf(txt.toLowerCase()) != -1) {
                    sub1.add(listaTodos.get(i));
                }
            }
            for (int i = 0; i < listaAprendidos.size(); i++) {
                if (listaAprendidos.get(i).getNombre().toLowerCase().indexOf(txt.toLowerCase()) != -1) {
                    sub2.add(listaAprendidos.get(i));
                }
            }
            for (int i = 0; i < listaPreparados.size(); i++) {
                if (listaPreparados.get(i).getNombre().toLowerCase().indexOf(txt.toLowerCase()) != -1) {
                    sub3.add(listaPreparados.get(i));
                }
            }
            vistaTodos.setAdapter(new AdaptadorHechizo(this, sub1));
            vistaAprendidos.setAdapter(new AdaptadorHechizo(this, sub2));
            vistaPreparado.setAdapter(new AdaptadorHechizo(this, sub3));        //AdaptadorHechizo adapter = ((AdaptadorHechizo) vistaTodos.getAdapter());
            //adapter.setLista(sub1);
            //adapter = ((AdaptadorHechizo) vistaAprendidos.getAdapter());
            //adapter.setLista(sub2);
            //adapter = ((AdaptadorHechizo) vistaPreparado.getAdapter());
            //adapter.setLista(sub3);

        }
    }
}
