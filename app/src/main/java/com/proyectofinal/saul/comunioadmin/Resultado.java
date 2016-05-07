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
    String NAMESPACE = "http://rpc.comunio.es/soapservice.php";
    String URL="http://rpc.comunio.es/soapservice.php/getuserid";
    String METHOD_NAME = "getuserid";
    String SOAP_ACTION = "http://rpc.comunio.es/soapservice.php/";

    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

    HttpTransportSE transporte = new HttpTransportSE(URL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        //Localizar los controles
        txtUsuario = (TextView) findViewById(R.id.txtSaludo);

        //Recuperamos la información pasada en el intent
        Bundle bundle = this.getIntent().getExtras();

        request.addProperty("usuario", bundle.getString("USUARIO"));

        //envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        try
        {
            transporte.call(SOAP_ACTION, envelope);

            SoapPrimitive resultado_xml =(SoapPrimitive)envelope.getResponse();
            String res = resultado_xml.toString();

            if(res.equals("1"))
                txtUsuario.setText("reeeeee");
        }
        catch (Exception e)
        {
            txtUsuario.setText("Error!");
        }

        //Construimos el mensaje a mostrar
        //txtUsuario.setText("Hola " + bundle.getString("USUARIO"));
        //Vamos a probar
    }
}
