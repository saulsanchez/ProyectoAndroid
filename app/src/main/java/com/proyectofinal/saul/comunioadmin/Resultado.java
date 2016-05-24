package com.proyectofinal.saul.comunioadmin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.Console;
import java.util.Vector;

public class Resultado extends AppCompatActivity {

    //TextView txtUsuario;
    private MyAsyncTask myAsincTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        //Recuperamos la información pasada en el intent
        Bundle bundle = this.getIntent().getExtras();
        String user = bundle.getString("USUARIO");
        myAsincTask = new MyAsyncTask();
        myAsincTask.execute(user);
    }

    private class MyAsyncTask extends AsyncTask<String, Integer, String> {
        private ProgressDialog dialog = new ProgressDialog(Resultado.this);
        String NAMESPACE = "http://rpc.comunio.es/soapservice.php?wsdl";
        String URL = "http://rpc.comunio.es/soapservice.php";

        SoapObject request;
        SoapSerializationEnvelope envelope;
        HttpTransportSE transporte;

        String getUserId = "getuserid"; //Id usuario por nombre
        String getCommunityId = "getcommunityid"; //id comunidad por id usuario
        String getUsersId = "getuserids"; //ids de usuarios de la comunidad por id de comunidad
        String getUsersFirstName = "getusersfirstname"; //Primer nombre de los usuarios
        String getUserGameDayPoints = "getusergamedaypoints"; // Puntos de usuario de liga por id de usuario y jornada


        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Calculando puntuaciones, por favor, espere...");
            this.dialog.show();
            this.dialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected String doInBackground(String... params) {
            request = new SoapObject(NAMESPACE, getUserId);
            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            transporte = new HttpTransportSE(URL);

            String res = "";
            String nombreUsuario = "USUARIO: " + params[0] + "\n";
            String idUsuario = "";
            String idComunidad = "";
            String vectorUsuarios = "";
            String nombreUsuarios = "";
            String puntosUsuarios = "";

            request.addProperty("login", params[0]);

            envelope.setOutputSoapObject(request);

            try {
                transporte.call(URL + "/" + getUserId, envelope);
                int userId = (int) envelope.getResponse();
                idUsuario = "USER ID: " + userId + "\n";

                //Con el id de usuario se saca el id de comunidad
                request = new SoapObject(NAMESPACE, getCommunityId);
                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                transporte = new HttpTransportSE(URL);
                request.addProperty("userid", userId);
                envelope.setOutputSoapObject(request);
                transporte.call(URL + "/" + getCommunityId, envelope);
                int communityId = (int) envelope.getResponse();
                idComunidad = "COMUNIDAD: " + communityId + "\n";

                //Con el id de la comunidad se sacan todos los id de usuario de esa comunidad
                request = new SoapObject(NAMESPACE, getUsersId);
                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                transporte = new HttpTransportSE(URL);
                request.addProperty("communityid", communityId);
                envelope.setOutputSoapObject(request);
                transporte.call(URL + "/" + getUsersId, envelope);
                Vector communityIds = (Vector) envelope.getResponse();
                vectorUsuarios = "IDS DE COMUNIDAD: " + "\n";
                for (int i = 0; i < communityIds.size(); i++)
                    vectorUsuarios += "    NOMBRE: " + communityIds.get(i) + "\n";

                //Nombre usuarios por id
                nombreUsuarios = "NOMBRES: " + "\n";
                Vector<String> userVector = new Vector<String>();
                for (int i = 0; i < communityIds.size(); i++) {
                    request = new SoapObject(NAMESPACE, getUsersFirstName);
                    envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    transporte = new HttpTransportSE(URL);
                    request.addProperty("userid", communityIds.get(i));
                    envelope.setOutputSoapObject(request);
                    transporte.call(URL + "/" + getUsersFirstName, envelope);
                    String name = (String) envelope.getResponse();
                    userVector.add(name);
                    nombreUsuarios += "    NOMBRE: " + name + "\n";
                }

                //Puntos por id usuario
                int jornada = 38;
                puntosUsuarios = "PUNTOS DE JORNADA " + jornada + ": " + "\n";
                Vector<String> userPoints = new Vector<String>();
                for (int i = 0; i < communityIds.size(); i++) {
                    request = new SoapObject(NAMESPACE, getUserGameDayPoints);
                    envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    transporte = new HttpTransportSE(URL);

                    request.addProperty("userid", communityIds.get(i));
                    request.addProperty("gameday", jornada);

                    envelope.setOutputSoapObject(request);
                    try {
                        transporte.call(URL + "/" + getUserGameDayPoints, envelope);
                        int points = (int) envelope.getResponse();
                        userPoints.add("" + points);
                        puntosUsuarios += "    USUARIO: " + communityIds.get(i) + " PUNTOS: " + points + "\n";
                    }
                    catch(Exception e){
                        puntosUsuarios += "    USUARIO: " + communityIds.get(i) + " NO ACCESIBLE\n";
                    }
                }

            } catch (Exception e) {
                res = "ERROR: " + e.getMessage() + "\n";
                //e.printStackTrace();
            }
            res = nombreUsuario + idUsuario + idComunidad + vectorUsuarios + nombreUsuarios + puntosUsuarios + "CACA";

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            TextView txt = (TextView) findViewById(R.id.txtResultado);
            txt.setText(result);
        }


    }
}