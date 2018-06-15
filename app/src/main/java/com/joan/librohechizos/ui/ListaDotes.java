package com.joan.librohechizos.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import com.joan.librohechizos.R;
import com.joan.librohechizos.modelo.Dote;
import com.joan.librohechizos.modelo.Hechizo;
import com.joan.librohechizos.modelo.Personaje;
import com.joan.librohechizos.sqlite.OperacionesBD;
import com.joan.librohechizos.utiles.AdaptadorHechizo;
import com.joan.librohechizos.utiles.AdaptadorListaDote;

import java.util.ArrayList;

/**
 * Created by Joan on 27/01/2018.
 */

public class ListaDotes extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Personaje personaje;
    private TabHost TbH;
    DrawerLayout drawerLayout;
    //private OperacionesBD datos;
    // private ArrayList<Dote> dotes, dotesAprendidos;
    private ListView dotesView, dotesAprendidosView;
    private boolean estadoClkeando;
    private Button botonIzquierdo, botonDerecho;
    private NavigationView navigation;
    private DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.listar_dotes);
        TbH = (TabHost) findViewById(R.id.tab_dote); //llamamos al Tabhost
        TbH.setup();//lo activamos


        TabHost.TabSpec todos = TbH.newTabSpec("tab1");  //aspectos de cada Tab (pestaña)
        TabHost.TabSpec aprendidos = TbH.newTabSpec("tab2");

        todos.setIndicator("Todos");    //qué queremos que aparezca en las pestañas
        todos.setContent(R.id.tab_dotes_todos); //definimos el id de cada Tab (pestaña)

        aprendidos.setIndicator("Aprendidos");
        aprendidos.setContent(R.id.tab_dotes_aprendidos);

        TbH.addTab(todos); //añadimos los tabs ya programados
        TbH.addTab(aprendidos);

        dotesView = (ListView) findViewById(R.id.lista_todo_dote);
        dotesAprendidosView = (ListView) findViewById(R.id.lista_aprendido_dote);

        this.personaje = new Personaje(getIntent().getStringExtra("idPersonaje"));

        this.personaje = Personaje.obtener(this.personaje, getApplicationContext());

        listarTodosLosDotes();
        listarTodosLosDotesAprendidos();

        botonIzquierdo = (Button) findViewById(R.id.btn_dote_izquierdo);
        botonDerecho = (Button) findViewById(R.id.btn_dote_derecho);


        //inicializan sin ser vistos
        cambiarDeEstado(false);

        dotesView.setFooterDividersEnabled(true);
        dotesAprendidosView.setFooterDividersEnabled(true);


        dotesView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        dotesAprendidosView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


        agregarFuncionalidadMostrarDoteClick(dotesView);
        agregarFuncionalidadMostrarDoteClick(dotesAprendidosView);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigation = (NavigationView) findViewById(R.id.nav_view);

        navigation.setItemIconTintList(null);
        navigation.setNavigationItemSelectedListener(this);

        cambiarDeEstado(false);

        funcionalidadBotonDerecho();
        funcionalidadBotonIzquierdo();
        agregarFuncionalidadLongClickTodo();
        agregarFuncionalidadLongClickAprendidos();


    }

    private void funcionalidadBotonIzquierdo() {
        botonIzquierdo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (botonIzquierdo.getText().equals("Borrar")) {
                    olvidarSeleccionado();

                }

                else if (botonIzquierdo.getText().equals("Aprender")) {
                    aprenderSeleccionados();

                }
                cambiarDeEstado(false);
                listarTodosLosDotesAprendidos();
            }
        });
    }

    private void funcionalidadBotonDerecho() {
        botonDerecho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarDeEstado(false);
                cancelar( dotesView);
                cancelar(dotesAprendidosView);
            }
        });
    }

    private void cancelar(ListView vista) {
        SparseBooleanArray seleccionado = vista.getCheckedItemPositions();
        int size = vista.getCheckedItemPositions().size();
        if (!(seleccionado == null)) {
            for (int j = 0; j < size; j++) {
                //Si valueAt(j) es true, es que estaba seleccionado
                if (seleccionado.valueAt(j)) {
                    //en keyAt(j) obtengo su posición
                    int i = seleccionado.keyAt(j);

                    Personaje.aprenderDote(personaje,(Dote) vista.getItemAtPosition(i),getApplicationContext());
                    vista.setItemChecked(i, false);
                }
            }
        }
    }

    private void aprenderSeleccionados() {
        SparseBooleanArray seleccionado = dotesView.getCheckedItemPositions();
        int size = dotesView.getCheckedItemPositions().size();
        if (!(seleccionado == null)) {
            for (int j = 0; j < size; j++) {
                //Si valueAt(j) es true, es que estaba seleccionado
                if (seleccionado.valueAt(j)) {
                    //en keyAt(j) obtengo su posición
                    int i = seleccionado.keyAt(j);

                    Personaje.aprenderDote(personaje,(Dote) dotesView.getItemAtPosition(i),getApplicationContext());
                    dotesView.setItemChecked(i, false);
                }
            }
            Toast toast = Toast.makeText(getApplicationContext(), "dotes aprendidos", Toast.LENGTH_SHORT);
            toast.show();

        }
    }

    private void olvidarSeleccionado() {
        SparseBooleanArray seleccionado = dotesAprendidosView.getCheckedItemPositions();
        int size = dotesAprendidosView.getCheckedItemPositions().size();
        if (!(seleccionado == null)) {
            for (int j = 0; j < size; j++) {
                //Si valueAt(j) es true, es que estaba seleccionado
                if (seleccionado.valueAt(j)) {
                    //en keyAt(j) obtengo su posición
                    int i = seleccionado.keyAt(j);

                    Personaje.olvidarDote(personaje,(Dote) dotesAprendidosView.getItemAtPosition(i),getApplicationContext());
                    dotesAprendidosView.setItemChecked(i, false);
                }
            }
            Toast toast = Toast.makeText(getApplicationContext(), "dotes olvidados", Toast.LENGTH_SHORT);
            toast.show();

        }
    }

    private boolean funcionLongClick(final ListView vista, int pos) {
        //retorna true si hay que agregar etiquetas o false si no hay que agregarlas
        boolean res = false;
        if (vista.isItemChecked(pos)) {
            vista.setItemChecked(pos, false);
            if (vista.getCheckedItemCount() == 0) {
                cambiarDeEstado(false);

            }

        } else {
            if (vista.getCheckedItemCount() == 0) {
                cambiarDeEstado(true);
                res = true;

            }
            vista.setItemChecked(pos, true);
        }
        return res;
    }

    private boolean AgregarEtiquetasBotones(String[] etiquetas) {
        botonIzquierdo.setText(etiquetas[0]);
        botonDerecho.setText(etiquetas[1]);
        return true;
    }

    private boolean cambiarDeEstado(boolean estadoNuevo) {
        estadoClkeando = estadoNuevo;
        //puede ser gone o visible
        int vista;
        if (estadoNuevo) {
            vista = View.VISIBLE;
        } else {
            vista = View.GONE;

        }
        botonDerecho.setVisibility(vista);
        botonIzquierdo.setVisibility(vista);
        return true;
    }

    private void listarTodosLosDotes() {
        ArrayList<Dote> dotes = Dote.obtenerTodosLosDotes(getApplicationContext());
        AdaptadorListaDote adp = new AdaptadorListaDote(this, dotes);
        dotesView.setAdapter(adp);
    }

    private void listarTodosLosDotesAprendidos() {
        ArrayList<Dote> dotes = Dote.obtenerDotesAprendidos(getApplicationContext(), personaje);
        AdaptadorListaDote adp = new AdaptadorListaDote(this, dotes);
        dotesAprendidosView.setAdapter(adp);
    }

    private void agregarFuncionalidadMostrarDoteClick(final ListView vista) {
        vista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Dote dote = (Dote) vista.getItemAtPosition(i);
                if (!estadoClkeando) {
                    vista.setItemChecked(i, false);
                    Intent intent = new Intent(ListaDotes.this, MostrarDote.class);
                    intent.putExtra("idDote", dote.getIdDote());
                    startActivity(intent);
                } else {

                    if (vista.getCheckedItemCount() == 0) {
                        cambiarDeEstado(false);
                        //mSearchView.setVisibility(View.VISIBLE);
                        //hacerInvisibleBotones();
                    }


                }
            }


        });
    }

    private void agregarFuncionalidadLongClickTodo() {
        dotesView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                if (funcionLongClick(dotesView, i)) {
                    String[] etiquetas = {"Aprender", "Cancelar"};
                    AgregarEtiquetasBotones(etiquetas);
                }
              /*
                if (dotesView.isItemChecked(i)) {
                    dotesView.setItemChecked(i, false);
                    if (dotesView.getCheckedItemCount() == 0) {
                        cambiarDeEstado(false);

                    }

                } else {
                    if (dotesView.getCheckedItemCount() == 0) {
                        cambiarDeEstado(true);
                        String[] texto = {"aprender", "cancelar"};

                    }
                    dotesView.setItemChecked(i, true);
                }*/

                return true;
            }
        });
    }

    private void agregarFuncionalidadLongClickAprendidos() {
        dotesAprendidosView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                if (funcionLongClick(dotesAprendidosView, i)) {
                    String[] etiquetas = {"Borrar", "Cancelar"};
                    AgregarEtiquetasBotones(etiquetas);
                }
              /*
                if (dotesView.isItemChecked(i)) {
                    dotesView.setItemChecked(i, false);
                    if (dotesView.getCheckedItemCount() == 0) {
                        cambiarDeEstado(false);

                    }

                } else {
                    if (dotesView.getCheckedItemCount() == 0) {
                        cambiarDeEstado(true);
                        String[] texto = {"aprender", "cancelar"};

                    }
                    dotesView.setItemChecked(i, true);
                }*/

                return true;
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case (R.id.menu_dotes):
                /*intent = new Intent(this, ListaDotes.class);
                intent.putExtra("idPersonaje", "-1");
                startActivity(intent);*/
                break;
            case (R.id.menu_hechizos):
                intent = new Intent(this, LibroDeHechizos.class);
                intent.putExtra("idPersonaje", personaje.getIdPersonaje());
                startActivity(intent);
            default:
                break;

        }
        return true;
    }
}