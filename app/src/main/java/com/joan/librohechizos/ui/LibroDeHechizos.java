package com.joan.librohechizos.ui;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TableRow;
import android.widget.Toast;

import com.joan.librohechizos.R;
import com.joan.librohechizos.modelo.Clase;
import com.joan.librohechizos.modelo.Escuela;
import com.joan.librohechizos.modelo.Hechizo;
import com.joan.librohechizos.modelo.Personaje;
import com.joan.librohechizos.sqlite.OperacionesBD;
import com.joan.librohechizos.utiles.AdaptadorHechizo;
import com.joan.librohechizos.utiles.FiltroHechizo;

import java.util.ArrayList;

public class LibroDeHechizos extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private Personaje personaje;
    private TabHost TbH;
    private OperacionesBD datos;
    private ArrayList<Hechizo> listaAprendidos, listaPreparados, listaTodos;
    private String filtro, textoFiltro;
    private Button btnFiltro, botonIzquierda, botonCentral, botonDerecha;
    private FiltroHechizo popup;
    private boolean clickFiltro, estadoClkeando;
    private ListView vistaTodos, vistaAprendidos, vistaPreparado;
    private SearchView mSearchView;
    private TableRow listaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.libro_de_hechizos);
        clickFiltro = false;
        datos = OperacionesBD.obtenerInstancia(getApplicationContext());
        filtro = "";
        TbH = (TabHost) findViewById(R.id.tab_libro); //llamamos al Tabhost
        TbH.setup();//lo activamos
        //inciadilizo objetos que su usan para aprender o eliminar

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

        vistaTodos.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        vistaAprendidos.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        vistaPreparado.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


        //agregando funcionalidad cuando se clikean los items
        agregarFuncionalidadMostrarHechizoClick(vistaTodos);
        agregarFuncionalidadMostrarHechizoClick(vistaAprendidos);
        agregarFuncionalidadMostrarHechizoClick(vistaPreparado);

        agregarFuncionalidadLongClickTodo();
        agregarFuncionalidadLongclikAprendido();
        agregarFuncionalidadLongClickPreparados();

        botonIzquierda = (Button) findViewById(R.id.btn_izquierda);
        botonCentral = (Button) findViewById(R.id.btn_central);
        botonDerecha = (Button) findViewById(R.id.btn_derecha);


        listaView = (TableRow) findViewById(R.id.row_abajo);

        hacerInvisibleBotones();

        estadoClkeando = false;

        funcionalidadBotonIzquierdo();
        funcionalidadBotonCentral();
        funcionalidadBotonDerecho();


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

    private void limpiarCk() {
        listarHechizosAprendidos();
        //filtroTexto(textoFiltro);
        ArrayList<Hechizo> sub1, sub2, sub3;
        sub2 = filtroDeTextoAux(listaAprendidos, textoFiltro);
        sub3 = filtroDeTextoAux(listaPreparados, textoFiltro);
        vistaAprendidos.setAdapter(new AdaptadorHechizo(this, sub2));
        vistaPreparado.setAdapter(new AdaptadorHechizo(this, sub3));
        estadoClkeando = false;
        mSearchView.setVisibility(View.VISIBLE);
        hacerInvisibleBotones();


    }

    private void funcionalidadBotonDerecho() {
        botonDerecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liberarSeleccionados(vistaTodos);
                liberarSeleccionados(vistaAprendidos);
                liberarSeleccionados(vistaPreparado);
                listarHechizosPreparados();
                listarHechizosAprendidos();
                filtroTexto(textoFiltro);
                estadoClkeando = false;
                mSearchView.setVisibility(View.VISIBLE);
                hacerInvisibleBotones();
            }
        });
    }

    private void funcionalidadBotonIzquierdo() {
        botonIzquierda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto = botonIzquierda.getText().toString().toLowerCase();

                switch (texto) {
                    case ("aprender"):
                        aprenderSeleccionados(vistaTodos);
                        liberarSeleccionados(vistaAprendidos);
                        liberarSeleccionados(vistaPreparado);
                        limpiarCk();
                        break;
                    case ("dejar"):
                        dejarPrepararSeleccionados(vistaPreparado);
                        liberarSeleccionados(vistaTodos);
                        liberarSeleccionados(vistaAprendidos);
                        listarHechizosPreparados();
                        limpiarCk();
                        break;
                    case ("preparar"):
                        prepararSeleccionados(vistaAprendidos);
                        liberarSeleccionados(vistaTodos);
                        liberarSeleccionados(vistaPreparado);
                        listarHechizosPreparados();
                        listarHechizosAprendidos();
                        filtroTexto(textoFiltro);
                        estadoClkeando = false;
                        mSearchView.setVisibility(View.VISIBLE);
                        hacerInvisibleBotones();
                }
            }
        });
    }


    private void funcionalidadBotonCentral() {
        botonCentral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto = botonCentral.getText().toString().toLowerCase();

                switch (texto) {
                    case ("cancelar"):
                        liberarSeleccionados(vistaTodos);
                        liberarSeleccionados(vistaPreparado);
                        liberarSeleccionados(vistaAprendidos);
                        limpiarCk();
                        break;
                    case ("eliminar"):
                        eliminarSeleccionados(vistaAprendidos);
                        liberarSeleccionados(vistaTodos);
                        liberarSeleccionados(vistaAprendidos);
                        liberarSeleccionados(vistaPreparado);
                        listarHechizosPreparados();
                        listarHechizosAprendidos();
                        filtroTexto(textoFiltro);
                        estadoClkeando = false;
                        mSearchView.setVisibility(View.VISIBLE);
                        hacerInvisibleBotones();
                        break;
                }
            }
        });
    }


    private void eliminar(int i, ListView vista) {
        final AdaptadorHechizo adapter = (AdaptadorHechizo) vista.getAdapter();
        Hechizo hechizo = adapter.getListaActual().get(i);
        String idHechizo = hechizo.getIdHechizo();
        if (datos.estaAprendido(personaje.getIdPersonaje(), idHechizo)) {
            try {
                datos.getDb().beginTransaction();
                datos.eliminarHechizoAprendido(personaje.getIdPersonaje(), adapter.getListaActual().get(i).getIdHechizo());


                datos.getDb().setTransactionSuccessful();
            } finally {
                datos.getDb().endTransaction();
            }


        }
    }

    private void eliminarSeleccionados(ListView vista) {
        SparseBooleanArray seleccionado = vista.getCheckedItemPositions();
        int size = vista.getCheckedItemPositions().size();

        if (!(seleccionado == null)) {
            for (int j = 0; j < size; j++) {
                //Si valueAt(j) es true, es que estaba seleccionado
                if (seleccionado.valueAt(j)) {
                    //en keyAt(j) obtengo su posición
                    int i = seleccionado.keyAt(j);
                    vista.setItemChecked(i, false);
                    eliminar(i, vista);
                }
            }

        }

    }


    private void preparar(int i, ListView vista) {
        final AdaptadorHechizo adapter = (AdaptadorHechizo) vista.getAdapter();
        Hechizo hechizo = adapter.getListaActual().get(i);
        String idHechizo = hechizo.getIdHechizo();
        try {
            datos.getDb().beginTransaction();
            datos.prepararHechizo(personaje.getIdPersonaje(), idHechizo);
            datos.getDb().setTransactionSuccessful();
        } finally {
            datos.getDb().endTransaction();

        }
    }

    private void prepararSeleccionados(ListView vista) {
        SparseBooleanArray seleccionado = vista.getCheckedItemPositions();
        int size = vista.getCheckedItemPositions().size();

        if (!(seleccionado == null)) {
            for (int j = 0; j < size; j++) {
                //Si valueAt(j) es true, es que estaba seleccionado
                if (seleccionado.valueAt(j)) {
                    //en keyAt(j) obtengo su posición
                    int i = seleccionado.keyAt(j);
                    vista.setItemChecked(i, false);
                    preparar(i, vista);
                }
            }

        }
    }


    private void dejarPreparar(int i, ListView vista) {
        final AdaptadorHechizo adapter = (AdaptadorHechizo) vista.getAdapter();
        Hechizo hechizo = adapter.getListaActual().get(i);
        String idHechizo = hechizo.getIdHechizo();
        try {
            datos.getDb().beginTransaction();
            datos.dejarDePreparaHechizo(idHechizo, personaje.getIdPersonaje());
            datos.getDb().setTransactionSuccessful();

        } finally {
            datos.getDb().endTransaction();
        }


    }


    private void dejarPrepararSeleccionados(ListView vista) {
        SparseBooleanArray seleccionado = vista.getCheckedItemPositions();
        int size = vista.getCheckedItemPositions().size();
        if (!(seleccionado == null)) {
            for (int j = 0; j < size; j++) {
                //Si valueAt(j) es true, es que estaba seleccionado
                if (seleccionado.valueAt(j)) {
                    //en keyAt(j) obtengo su posición
                    int i = seleccionado.keyAt(j);
                    dejarPreparar(i, vista);
                    vista.setItemChecked(i, false);
                }
            }

        }
    }


    private void liberarSeleccionados(ListView vista) {
        SparseBooleanArray seleccionado = vista.getCheckedItemPositions();
        int size = vista.getCheckedItemPositions().size();
        if (!(seleccionado == null)) {
            for (int j = 0; j < size; j++) {
                //Si valueAt(j) es true, es que estaba seleccionado
                //en keyAt(j) obtengo su posición
                int i = seleccionado.keyAt(j);
                vista.setItemChecked(i, false);
            }
        }


    }


    private void aprenderSeleccionados(ListView vista) {
        SparseBooleanArray seleccionado = vista.getCheckedItemPositions();
        int size = vista.getCheckedItemPositions().size();
        if (!(seleccionado == null)) {
            for (int j = 0; j < size; j++) {
                //Si valueAt(j) es true, es que estaba seleccionado
                if (seleccionado.valueAt(j)) {
                    //en keyAt(j) obtengo su posición
                    int i = seleccionado.keyAt(j);
                    aprender(i, vista);
                    vista.setItemChecked(i, false);
                }
            }
            Toast toast = Toast.makeText(getApplicationContext(), "hechizos aprendidos", Toast.LENGTH_SHORT);
            toast.show();

        }

    }

    private void aprender(int i, ListView vista) {
        final AdaptadorHechizo adapter = (AdaptadorHechizo) vista.getAdapter();
        Hechizo hechizo = adapter.getListaActual().get(i);
        String idHechizo = hechizo.getIdHechizo();
        if (!datos.estaAprendido(personaje.getIdPersonaje(), idHechizo)) {
            try {
                datos.getDb().beginTransaction();
                datos.insertarHechizoAprendido(idHechizo, personaje.getIdPersonaje());
                datos.getDb().setTransactionSuccessful();


            } finally {
                datos.getDb().endTransaction();
            }

        }
    }


    private void hacerInvisibleBotones() {
        botonDerecha.setVisibility(View.GONE);
        botonCentral.setVisibility(View.GONE);
        botonIzquierda.setVisibility(View.GONE);
        listaView.setVisibility(View.GONE);
    }

    private void hacerVisibleBotones(int cant, String[] textos) {
        //cant minimamente deberia ser 2
        if (cant >= 2 && textos.length >= 2) {
            hacerInvisibleBotones();
            listaView.setVisibility(View.VISIBLE);
            botonIzquierda.setVisibility(View.VISIBLE);
            botonIzquierda.setText(textos[0]);
            botonCentral.setVisibility(View.VISIBLE);
            botonCentral.setText(textos[1]);
            if (cant == 3 && textos.length == 3) {
                botonDerecha.setVisibility(View.VISIBLE);
                botonDerecha.setText(textos[2]);
            }
        }

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
                AdaptadorHechizo adapter = (AdaptadorHechizo) vistaTodos.getAdapter();
                if (!adapter.getListaActual().get(i).getNombre().equals("-1")) {

                    if (vistaTodos.isItemChecked(i)) {
                        vistaTodos.setItemChecked(i, false);
                        if (vistaTodos.getCheckedItemCount() == 0) {
                            estadoClkeando = false;
                            mSearchView.setVisibility(View.VISIBLE);
                            hacerInvisibleBotones();
                        }

                    } else {
                        if (vistaTodos.getCheckedItemCount() == 0) {
                            estadoClkeando = true;
                            mSearchView.setVisibility(View.INVISIBLE);
                            String[] texto = {"aprender", "cancelar"};
                            hacerVisibleBotones(2, texto);
                        }
                        vistaTodos.setItemChecked(i, true);
                    }
                }
                return true;
            }
        });
    }

    private void agregarFuncionalidadLongClickPreparados() {
        vistaPreparado.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                AdaptadorHechizo adapter = (AdaptadorHechizo) vistaPreparado.getAdapter();
                if (!adapter.getListaActual().get(i).getNombre().equals("-1")) {

                    if (vistaPreparado.isItemChecked(i)) {
                        vistaPreparado.setItemChecked(i, false);
                        if (vistaPreparado.getCheckedItemCount() == 0) {
                            estadoClkeando = false;
                            mSearchView.setVisibility(View.VISIBLE);
                            hacerInvisibleBotones();
                        }

                    } else {
                        if (vistaPreparado.getCheckedItemCount() == 0) {
                            estadoClkeando = true;
                            mSearchView.setVisibility(View.INVISIBLE);
                            String[] texto = {"dejar", "cancelar"};
                            hacerVisibleBotones(2, texto);
                        }
                        vistaPreparado.setItemChecked(i, true);
                    }
                }
                return true;
            }
        });
    }


    private void agregarFuncionalidadLongclikAprendido() {
        vistaAprendidos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                AdaptadorHechizo adapter = (AdaptadorHechizo) vistaAprendidos.getAdapter();
                if (!adapter.getListaActual().get(i).getNombre().equals("-1")) {

                    if (vistaAprendidos.isItemChecked(i)) {
                        vistaAprendidos.setItemChecked(i, false);
                        if (vistaAprendidos.getCheckedItemCount() == 0) {
                            estadoClkeando = false;
                            mSearchView.setVisibility(View.VISIBLE);
                            hacerInvisibleBotones();
                        }

                    } else {
                        if (vistaAprendidos.getCheckedItemCount() == 0) {
                            estadoClkeando = true;
                            mSearchView.setVisibility(View.INVISIBLE);
                            String[] texto = {"preparar", "eliminar", "cancelar"};
                            hacerVisibleBotones(3, texto);
                        }
                        vistaAprendidos.setItemChecked(i, true);
                    }

                }
                return true;

            }
        });
    }

    private void agregarFuncionalidadMostrarHechizoClick(final ListView vista) {
        vista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Hechizo hechizo = (Hechizo) vista.getItemAtPosition(i);
                if (!estadoClkeando && !hechizo.getNombre().equals("-1")) {
                    vista.setItemChecked(i, false);
                    Intent intent = new Intent(LibroDeHechizos.this, MostrarHechizo.class);
                    intent.putExtra("idHechizo", hechizo.getIdHechizo());
                    startActivity(intent);
                } else {
                    if(hechizo.getNombre().equals("-1")){
                        vista.setItemChecked(i, false);
                    }
                    if (vista.getCheckedItemCount() == 0) {
                        estadoClkeando = false;
                        mSearchView.setVisibility(View.VISIBLE);
                        hacerInvisibleBotones();
                    }



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
            listaHechizos.moveToPrevious();
        }
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

   /* public void listarTodoLosHechizos() {
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
    }*/


    public void listarTodoLosHechizos() {
        Cursor listaHechizos = datos.obtenerHechizos2(filtro);
        listaTodos.clear();
        Cursor listarClases;
        if (listaHechizos.moveToNext()) {
            listaTodos.add(new Hechizo("-1", listaHechizos.getInt(4)));
        }
        listaHechizos.moveToPrevious();
        try {
            while (listaHechizos != null && listaHechizos.moveToNext()) {
                listarClases = datos.obtenerClasesDeHechizo(listaHechizos.getString(0));
                ArrayList<Clase> listaClase = new ArrayList<>();
                while (listarClases != null && listarClases.moveToNext()) {
                    listaClase.add(new Clase(listarClases.getString(0), listarClases.getString(1)));
                }
                Cursor escuela = datos.obtenerEscuela(listaHechizos.getString(3));
                Escuela esc = new Escuela("", "");
                if (escuela.moveToNext()) {
                    esc.setIdEscuela(escuela.getString(0));
                    esc.setNombre(escuela.getString(1));
                }
                if (!listaTodos.isEmpty() && listaTodos.get(listaTodos.size() - 1).getNivel() != listaHechizos.getInt(4)) {
                    listaTodos.add(new Hechizo("-1", listaHechizos.getInt(4)));
                }

                listaTodos.add(new Hechizo(listaHechizos.getString(0),listaHechizos.getString(1),listaHechizos.getInt(2),
                        esc,listaHechizos.getInt(4)));
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
                liberarSeleccionados(vistaTodos);
                liberarSeleccionados(vistaAprendidos);
                liberarSeleccionados(vistaPreparado);
                filtroTexto(textoFiltro);
                estadoClkeando = false;
                mSearchView.setVisibility(View.VISIBLE);
                hacerInvisibleBotones();
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
        textoFiltro = text;
        filtroTexto(textoFiltro);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        textoFiltro = newText;
        filtroTexto(textoFiltro);

        return false;
    }


    private ArrayList<Hechizo> filtroDeTextoAux(ArrayList<Hechizo> list, String txt) {
        ArrayList<Hechizo> subList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getNombre().toLowerCase().contains(txt.toLowerCase())
                    || list.get(i).getNombre().equals("-1")) {
                if (!subList.isEmpty()) {
                    if (list.get(i).getNombre().equals("-1")
                            && list.get(i).getNombre().equals(subList.get(subList.size() - 1).getNombre())) {
                                    subList.remove(subList.size() - 1);
                    }
                }
                subList.add(list.get(i));
            }
        }
        if (!subList.isEmpty() && subList.get(subList.size() - 1).getNombre().equals("-1")) {
            subList.remove(subList.size() - 1);
        }
        return subList;
    }


    private void filtroTexto(String txt) {
        ArrayList<Hechizo> sub1, sub2, sub3;
        sub1 = filtroDeTextoAux(listaTodos, txt);
        sub2 = filtroDeTextoAux(listaAprendidos, txt);
        sub3 = filtroDeTextoAux(listaPreparados, txt);
        vistaTodos.setAdapter(new AdaptadorHechizo(this, sub1));
        vistaAprendidos.setAdapter(new AdaptadorHechizo(this, sub2));
        vistaPreparado.setAdapter(new AdaptadorHechizo(this, sub3));
    }
}
