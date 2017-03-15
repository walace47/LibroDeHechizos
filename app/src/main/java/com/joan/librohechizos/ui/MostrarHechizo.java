package com.joan.librohechizos.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.joan.librohechizos.R;
import com.joan.librohechizos.modelo.Hechizo;
import com.joan.librohechizos.sqlite.OperacionesBD;
import com.joan.librohechizos.utiles.ComunicadorDeObjetos;

public class MostrarHechizo extends AppCompatActivity {
    private Hechizo hechizo;
    private OperacionesBD datos;
    TextView nombre, rango, nivelEscuela, componentes;
    TextView descripcion, aMayorNivel, tiempoDeCasteo, duracion, clases;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostrar_hechizo);
        datos = OperacionesBD.obtenerInstancia(this);
        hechizo = (Hechizo) ComunicadorDeObjetos.getMensaje();
        asignarTextViewConSuId();

    }

    private void asignarTextViewConSuId() {
        clases = (TextView) findViewById(R.id.txt_hechizo_clases);
        clases.setText("");
        int listaClaseTamanio = hechizo.getClases().size();
        for (int i = 0; i < listaClaseTamanio; i++) {
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
        tiempoDeCasteo.setText("CASTEO: " + hechizo.getTiempoDeCasteo());
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
        if (hechizo.getComponenteVerbal() == 1) {

            if (!componentes.getText().equals("COMPONENTES: ")) {
                componentes.setText(componentes.getText() + ",");
            }
            if (hechizo.getComponenteMaterial() == 1) {
                componentes.setText(componentes.getText() + "M");
                if (!hechizo.getDescripcionDelComponenteMaterial().equals("")) {
                    componentes.setText(componentes.getText() + "(" + hechizo.getDescripcionDelComponenteMaterial() + ")");
                }
            }

        }
        descripcion = (TextView) findViewById(R.id.txt_descripcion);
        descripcion.setText(hechizo.getDescripcion());
        duracion = (TextView) findViewById(R.id.txt_duracion);
        if (hechizo.getConcentracion() == 1) {
            duracion.setText("DURACION: " + "concentracion, " + hechizo.getDuracion());
        } else {
            duracion.setText("DURACION: " + hechizo.getDuracion());
        }
        aMayorNivel = (TextView) findViewById(R.id.txt_a_mayor_nivel);
        aMayorNivel.setText(hechizo.getaMayorNivel());
    }



}
