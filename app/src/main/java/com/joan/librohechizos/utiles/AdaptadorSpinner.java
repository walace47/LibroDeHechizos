package com.joan.librohechizos.utiles;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.joan.librohechizos.Interfazes.Listable;
import com.joan.librohechizos.R;
import com.joan.librohechizos.modelo.Clase;
import com.joan.librohechizos.ui.CrearPersonaje;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Joan on 10/03/2017.
 */

    public class AdaptadorSpinner extends ArrayAdapter<Listable> {
    private AppCompatActivity contexto;

        public AdaptadorSpinner(Context context, int textViewResourceId, LinkedList<Listable> objects) {
            super(context, textViewResourceId, objects);
            contexto= (AppCompatActivity) context;
        }

    /*public AdaptadorSpinner(CrearPersonaje context, int spiner_personalizado, LinkedList<Listable> clases) {
        super();
    }*/

    @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            //AppCompatActivity contexto;
           // getContext().
            LayoutInflater inflater=contexto.getLayoutInflater();
            View row=inflater.inflate(R.layout.spiner_personalizado, parent, false);
            TextView label=(TextView)row.findViewById(R.id.txt_nombre);
            label.setText(this.getItem(position).toString());


            return row;
        }
    }


