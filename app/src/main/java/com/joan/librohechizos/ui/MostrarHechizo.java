package com.joan.librohechizos.ui;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.widget.TextView;

import com.joan.librohechizos.R;
import com.joan.librohechizos.modelo.Clase;
import com.joan.librohechizos.modelo.Escuela;
import com.joan.librohechizos.modelo.Hechizo;
import com.joan.librohechizos.sqlite.OperacionesBD;

import java.util.ArrayList;

public class MostrarHechizo extends AppCompatActivity {
    private Hechizo hechizo;
    private OperacionesBD datos;
    private TextView nombre, rango, nivelEscuela, componentes;
    private TextView  aMayorNivel, tiempoDeCasteo, duracion, clases;
    private WebView descripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.mostrar_hechizo);
        datos = OperacionesBD.obtenerInstancia(this);
        hechizo = obtenerHechizo(getIntent().getStringExtra("idHechizo"));
        asignarTextViewConSuId();

    }

    protected void asignarTextViewConSuId() {
        clases = (TextView) findViewById(R.id.txt_hechizo_clases);
        int listaClaseTamanio = hechizo.getClases().size();
       clases.setText(hechizo.getClases().get(0).getNombre());
        for (int i = 1; i < listaClaseTamanio; i++) {
            clases.setText(clases.getText() + ", " + hechizo.getClases().get(i).getNombre());
        }
        nombre = (TextView) findViewById(R.id.txt_hechizo_nombre);
        nombre.setText(hechizo.getNombre());
        if (hechizo.getRitual() == 1) {
            nombre.setText(nombre.getText() + "(RITUAL)");
        }
        rango = (TextView) findViewById(R.id.txt_rango);
        switch (hechizo.getRango()) {
            case (0):
                rango.setText("RANGO: " + "A ti mismo");
                break;
            case (1):
                rango.setText("RANGO: " + "Tocar");
                break;
            default:
                rango.setText("RANGO: " + hechizo.getRango() + " pies");
                break;

        }
        nivelEscuela = (TextView) findViewById(R.id.txt_hechizo_escuela_nivel);
        nivelEscuela.setText(hechizo.getEscuela().getNombre() + " Nivel: " + hechizo.getNivel());
        tiempoDeCasteo = (TextView) findViewById(R.id.txt_coste);
        tiempoDeCasteo.setText("TIEMPO DE CASTEO: " + hechizo.getTiempoDeCasteo());
        componentes = (TextView) findViewById(R.id.txt_componentes);
        componentes.setText("COMPONENTES: ");
        if (hechizo.getComponenteVerbal() == 1) {
            componentes.setText(componentes.getText() + "V");
        }
        if (hechizo.getComponenteSomatico() == 1) {
            if (!componentes.getText().equals("COMPONENTES: ")) {
                componentes.setText(componentes.getText() + ",");
            }
            componentes.setText(componentes.getText() + "S");

        }
            if (hechizo.getComponenteMaterial() == 1) {
                if (!componentes.getText().equals("COMPONENTES: ")) {
                    componentes.setText(componentes.getText() + ",");
                }
                componentes.setText(componentes.getText() + "M");
                if (!hechizo.getDescripcionDelComponenteMaterial().equals("")) {
                    componentes.setText(componentes.getText() + "(" + hechizo.getDescripcionDelComponenteMaterial() + ")");
                }
            }

        descripcion = (WebView) findViewById(R.id.txt_descripcion);
        String description="<html><body><head><style type=\"text/css\">\n" +
                "p {\n" +
                "font-size:90%;text-indent: 1em; text-align:justify; \n" +
                "}\n" +"body {font-family: Arial, Helvetica, sans-serif;}\n" +
                "\n" +
                "table {     font-family: \"Lucida Sans Unicode\", \"Lucida Grande\", Sans-Serif;\n" +
                "    font-size: 12px;     width: 100%; text-align: left;    border-collapse: collapse; }\n" +
                "\n" +
                "th {     font-size: 13px;     font-weight: normal;          background: #b9c9fe;\n" +
                "    border-top: 4px solid #aabcfe;    border-bottom: 1px solid #fff; color: #039; }\n" +
                "\n" +
                "td {         background: #e8edff;     border-bottom: 1px solid #fff;\n" +
                "    color: #669;    border-top: 1px solid transparent; }\n" +
                "\n" +
                "tr:hover td { background: #d0dafd; color: #339; }"+
                "</style></head><p>"+hechizo.getDescripcion()+"</p></body></html>";
        descripcion.loadData(description,"text/html; charset=UTF-8",null);
        duracion = (TextView) findViewById(R.id.txt_duracion);
        if (hechizo.getConcentracion() == 1) {
            duracion.setText("DURACION: " + "Concentración, " + hechizo.getDuracion());
        } else {
            duracion.setText("DURACION: " + hechizo.getDuracion());
        }
        aMayorNivel = (TextView) findViewById(R.id.txt_a_mayor_nivel);
        aMayorNivel.setText(hechizo.getaMayorNivel());
    }


    private Hechizo obtenerHechizo(String id){
            Hechizo hechizo=null;
            Cursor listaHechizos = datos.obtenerHechizo(id);
            try {
                    ArrayList<Clase> listaClase = new ArrayList<>();
                    while(listaHechizos.moveToNext()){
                        listaClase.add(new Clase(listaHechizos.getString(15), listaHechizos.getString(16)));
                    }
                    listaHechizos.moveToPrevious();

                    Cursor escuela = datos.obtenerEscuela(listaHechizos.getString(12));
                    Escuela esc = new Escuela("", "");
                    if (escuela.moveToNext()) {
                        esc.setIdEscuela(escuela.getString(0));
                        esc.setNombre(escuela.getString(1));
                    }

                    hechizo=new Hechizo(listaHechizos.getString(0), listaHechizos.getString(1),
                            listaHechizos.getString(2), listaHechizos.getString(3), listaHechizos.getInt(4),
                            listaHechizos.getInt(5), listaHechizos.getInt(6), listaHechizos.getInt(7), listaHechizos.getString(8),
                            listaHechizos.getInt(9), listaHechizos.getInt(10), listaHechizos.getString(11), esc,
                            listaHechizos.getInt(13), listaHechizos.getString(14), listaClase);
                    escuela.close();
            } finally {
                listaHechizos.close();

            }
            return hechizo;
        }


}
