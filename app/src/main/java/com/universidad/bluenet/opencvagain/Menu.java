package com.universidad.bluenet.opencvagain;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void Registrar(View v){
        Intent intent = new Intent(Menu.this,Registro.class);
        startActivity(intent);
    }

    public void Entrenar(View v){
        Toast.makeText(this,"Hacer el entrenamiento",Toast.LENGTH_SHORT).show();
    }

    public void Analizar(View v){
        Intent intent = new Intent(Menu.this,RegistroPython.class);
        startActivity(intent);
    }

}
