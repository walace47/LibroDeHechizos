package com.joan.librohechizos.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.joan.librohechizos.R;
import com.joan.librohechizos.modelo.Dote;

import org.w3c.dom.Text;

public class MostrarDote extends AppCompatActivity {
    private TextView nombre,requisitos;
    private WebView descripcion;
    private Dote dote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostar_dote);
        String id = getIntent().getStringExtra("idDote");
        dote = Dote.obtenerDote(id,getApplicationContext());
        asignarTextViewConSuId();
        if (dote != null){
            darValores();
        }
    }

    protected void asignarTextViewConSuId(){
        nombre = (TextView) findViewById(R.id.txt_dote_nombre);
        requisitos = (TextView) findViewById(R.id.txt_dote_requisito);
        descripcion = (WebView) findViewById(R.id.txt_dote_descripcion);
    }

    protected void darValores(){
        nombre.setText( dote.getNombre());
        requisitos.setText(dote.getRequisitos());
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
                "</style></head><p>"+dote.getDescripcion()+"</p></body></html>";
        descripcion.loadData(description,"text/html; charset=UTF-8",null);

    }
}
