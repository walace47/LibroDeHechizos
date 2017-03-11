package com.joan.librohechizos.utiles;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.joan.librohechizos.R;

import java.util.ArrayList;

/**
 * Created by Joan on 10/03/2017.
 */

    public class AdaptadorSpinner extends ArrayAdapter<Object> {
    AppCompatActivity contexto;

        public AdaptadorSpinner(Context context, int textViewResourceId, ArrayList<Object> objects) {
            super(context, textViewResourceId, objects);
            contexto= (AppCompatActivity) context;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater=contexto.getLayoutInflater();
            View row=inflater.inflate(R.layout.spiner_personalizado, parent, false);
            TextView label=(TextView)row.findViewById(R.id.txt_nombre);
            label.setText(this.getItem(position).toString());


            return row;
        }
    }


