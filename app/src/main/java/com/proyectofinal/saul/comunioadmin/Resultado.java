package com.proyectofinal.saul.comunioadmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class Resultado extends AppCompatActivity {

    TextView txtUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        //Localizar los controles
        txtUsuario = (TextView) findViewById(R.id.txtSaludo);

        //Recuperamos la información pasada en el intent
        Bundle bundle = this.getIntent().getExtras();

        txtUsuario.setText(bundle.get("USUARIO").toString());
    }
}
