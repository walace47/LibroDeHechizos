package com.joan.librohechizos.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.joan.librohechizos.R;

public class Donacion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donacion);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Button paypal = (Button) findViewById(R.id.donacion);
        paypal.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                Uri uri = Uri.parse("https://www.paypal.me/Dndlibrodehechizos");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }
}
