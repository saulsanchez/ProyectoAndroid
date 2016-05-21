package com.proyectofinal.saul.comunioadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView lblUsuario;
    private EditText txtUsuario;
    private Button btnAceptar;
    private MyAsyncTask myAsincTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Obtenemos una referencia a los controles de la interfaz
        lblUsuario = (TextView)findViewById(R.id.lblUsuario);
        txtUsuario = (EditText)findViewById(R.id.txtNombre);
        btnAceptar = (Button) findViewById(R.id.btnAceptar);

        //Implementamos el evento click del botón
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAsincTask = new MyAsyncTask();

                //Creamos el Intent
                Intent intent =
                        new Intent(MainActivity.this, Resultado.class);

                //Creamos la información a pasar entre actividades
                Bundle b = new Bundle();
                b.putString("USUARIO", txtUsuario.toString());

                //Añadimos la información al intent
                intent.putExtras(b);

                myAsincTask.execute();

                //Iniciamos la nueva actividad
                startActivity(intent);
            }
        });
    }
}
