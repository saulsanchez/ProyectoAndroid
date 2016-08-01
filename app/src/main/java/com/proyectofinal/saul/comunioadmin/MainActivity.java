package com.proyectofinal.saul.comunioadmin;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText txtUsuario;
    private Spinner spnJornada;
    private Button btnAceptar;
    private Intent intent;
    private Bundle b;

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.preferencias:
                intent = new Intent(MainActivity.this, Preferencias.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Obtenemos una referencia a los controles de la interfaz
        txtUsuario = (EditText)findViewById(R.id.txtNombre);
        spnJornada = (Spinner)findViewById(R.id.spnJornada);

        //Rellenamos el Spinner
        ArrayAdapter<CharSequence> adaptador = ArrayAdapter.createFromResource(this,
                R.array.jornada_array, R.layout.spinner_item);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnJornada.setAdapter(adaptador);

        btnAceptar = (Button) findViewById(R.id.btnAceptar);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Creamos el Intent
                Intent intent =
                        new Intent(MainActivity.this, Resultado.class);

                //Creamos la información a pasar entre actividades
                b = new Bundle();
                b.putString("USUARIO", txtUsuario.getText().toString());
                //b.putString("USUARIO", "tetin777");
                b.putInt("JORNADA", spnJornada.getSelectedItemPosition() + 1);

                //Añadimos la información al intent
                intent.putExtras(b);

                //Iniciamos la nueva actividad
                startActivity(intent);
            }
        });
    }
}