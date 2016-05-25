package com.proyectofinal.saul.comunioadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView lblUsuario;
    private EditText txtUsuario;
    private TextView lblJornada;
    private Spinner spnJornada;
    private Button btnAceptar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Obtenemos una referencia a los controles de la interfaz
        txtUsuario = (EditText)findViewById(R.id.txtNombre);
        spnJornada = (Spinner)findViewById(R.id.spnJornada);

        ArrayAdapter<CharSequence> adaptador = ArrayAdapter.createFromResource(this,
                R.array.jornada_array, R.layout.spinner_item);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnJornada.setAdapter(adaptador);

        btnAceptar = (Button) findViewById(R.id.btnAceptar);

        //Implementamos el evento click del botón
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Creamos el Intent
                Intent intent =
                        new Intent(MainActivity.this, Resultado.class);

                //Creamos la información a pasar entre actividades
                Bundle b = new Bundle();
                b.putString("USUARIO", txtUsuario.getText().toString());
                //b.putString("USUARIO", "tetin777");

                //Añadimos la información al intent
                intent.putExtras(b);



                //Iniciamos la nueva actividad
                startActivity(intent);
            }
        });
    }
}