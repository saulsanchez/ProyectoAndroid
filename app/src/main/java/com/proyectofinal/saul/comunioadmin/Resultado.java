package com.proyectofinal.saul.comunioadmin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class Resultado extends Activity {

    private ListView lstUsuarios;
    private List<Usuario> list;
    private AdaptadorUsuarios adaptador;
    private Bundle bundle;
    private String user;
    private Integer jornada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        list = new ArrayList<>();

        //Recuperamos la información pasada en el intent
        bundle = this.getIntent().getExtras();
        user = bundle.getString("USUARIO");
        jornada = bundle.getInt("JORNADA");

        adaptador = new AdaptadorUsuarios(this, list);

        lstUsuarios = (ListView)findViewById(R.id.LstUsuarios);

        if(adaptador!=null && lstUsuarios != null)
            lstUsuarios.setAdapter(adaptador);

        new MyAsyncTask(Resultado.this, list).execute(user, String.valueOf(jornada));
    }

    class AdaptadorUsuarios extends ArrayAdapter<Usuario> {

        private Activity context;
        private LayoutInflater inflater;
        private View item;
        private TextView lblNombre;
        private TextView lblPuntos;
        private TextView lblEstrellas;
        private TextView lblBeneficio;

        AdaptadorUsuarios(Activity context, List<Usuario> datos) {
            super(context, R.layout.listitem_usuarios, datos);
            this.context = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            inflater = context.getLayoutInflater();
            item = inflater.inflate(R.layout.listitem_usuarios, null);

            lblNombre = (TextView)item.findViewById(R.id.LblNombre);
            lblNombre.setText(list.get(position).getNombre());

            lblPuntos = (TextView)item.findViewById(R.id.LblPuntos);
            lblPuntos.setText("Puntos: " + list.get(position).getPuntos());

            lblEstrellas = (TextView)item.findViewById(R.id.LblEstrellas);
            lblEstrellas.setText("Jugadores estrella: " + list.get(position).getNumEstrellas());

            lblBeneficio = (TextView)item.findViewById(R.id.LblBeneficio);
            lblBeneficio.setText("Beneficio: " + list.get(position).getBeneficio());

            return (item);
        }
    }

    class MyAsyncTask extends AsyncTask<String, Void, Void> {
        private ProgressDialog dialog;
        private String NAMESPACE = "http://rpc.comunio.es/soapservice.php?wsdl";
        private String URL = "http://rpc.comunio.es/soapservice.php/";
        private Boolean existeUsuario = true;
        private Random r = new Random();

        private SoapObject request;
        private SoapSerializationEnvelope envelope;
        private HttpTransportSE transporte;

        private Usuario usuario;
        private List<Usuario> listaUsuarios;
        private Context context;

        private String getUserId = "getuserid"; //Id usuario por nombre
        private String getCommunityId = "getcommunityid"; //id comunidad por id usuario
        private String getCommunityName = "getcommunityname"; //Nombre de la liga
        private String getUsersId = "getuserids"; //ids de usuarios de la comunidad por id de comunidad
        private String getUsersFirstName = "getusersfirstname"; //Primer nombre de los usuarios
        private String getUserGameDayPoints = "getusergamedaypoints"; // Puntos de usuario de liga por id de usuario y jornada

        private Integer userId;
        private Integer communityId;
        private Vector communityIds;
        private String nombre;
        private String jornada;
        private Integer randPoints;
        private Integer randStars;
        private Integer points;

        private SharedPreferences preferences;

        private Integer precioPunto;
        private Integer precioEstrella;
        private Integer beneficioPuntos;
        private Integer beneficioEstrellas;
        private Integer beneficioTotal;

        public MyAsyncTask(Context context, List<Usuario> list){
            listaUsuarios = list;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            this.dialog.setMessage("Calculando puntuaciones, por favor, espere...");
            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            request = new SoapObject(NAMESPACE, getUserId);
            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            transporte = new HttpTransportSE(URL);

            request.addProperty("login", params[0]);

            envelope.setOutputSoapObject(request);

            try {
                transporte.call(URL + getUserId, envelope);
                userId = (int) envelope.getResponse();

                //Con el id de usuario se saca el id de comunidad
                request = new SoapObject(NAMESPACE, getCommunityId);
                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                transporte = new HttpTransportSE(URL);
                request.addProperty("userid", userId);
                envelope.setOutputSoapObject(request);
                transporte.call(URL + getCommunityId, envelope);
                communityId = (int) envelope.getResponse();

                //Con el id de la comunidad sacamos el nombre de la liga
                request = new SoapObject(NAMESPACE, getCommunityName);
                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                transporte = new HttpTransportSE(URL);
                request.addProperty("communityid", communityId);
                envelope.setOutputSoapObject(request);
                transporte.call(URL + getCommunityName, envelope);

                //Con el id de la comunidad se sacan todos los id de usuario de esa comunidad
                request = new SoapObject(NAMESPACE, getUsersId);
                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                transporte = new HttpTransportSE(URL);
                request.addProperty("communityid", communityId);
                envelope.setOutputSoapObject(request);
                transporte.call(URL + getUsersId, envelope);
                communityIds = (Vector) envelope.getResponse();

                //Nombre usuarios por id
                for (int i = 0; i < communityIds.size(); i++) {
                    request = new SoapObject(NAMESPACE, getUsersFirstName);
                    envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    transporte = new HttpTransportSE(URL);
                    request.addProperty("userid", communityIds.get(i));
                    envelope.setOutputSoapObject(request);
                    transporte.call(URL + getUsersFirstName, envelope);
                    nombre = (String) envelope.getResponse();
                    usuario = new Usuario();
                    usuario.setNombre(nombre);
                    listaUsuarios.add(usuario);
                }

                //Puntos por id usuario
                jornada = params[1];
                Integer numEstrellas = 0;
                for (int i = 0; i < communityIds.size(); i++) {
                    request = new SoapObject(NAMESPACE, getUserGameDayPoints);
                    envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    transporte = new HttpTransportSE(URL);

                    request.addProperty("userid", communityIds.get(i));
                    request.addProperty("gameday", jornada);

                    randPoints = r.nextInt(36) + 40;
                    randStars = r.nextInt(4);
                    numEstrellas += randStars;

                    envelope.setOutputSoapObject(request);
                    try {
                        transporte.call(URL + getUserGameDayPoints, envelope);
                        points = (int) envelope.getResponse();

                        listaUsuarios.get(i).setPuntos(points.toString());
                        Log.d("EEE", numEstrellas.toString());
                        listaUsuarios.get(i).setNumEstrellas((numEstrellas + randStars) <= 11 ? randStars.toString() : "0");
                        listaUsuarios.get(i).setBeneficio(calcularBeneficios(points, randStars));
                    }
                    catch(Exception e){
                        listaUsuarios.get(i).setPuntos(randPoints.toString());
                        Log.d("EEE", numEstrellas.toString());
                        listaUsuarios.get(i).setNumEstrellas((numEstrellas + randStars) <= 11 ? randStars.toString() : "0");
                        listaUsuarios.get(i).setBeneficio(calcularBeneficios(randPoints, randStars));
                    }
                }

                Collections.sort(listaUsuarios, new Comparator<Usuario>() {
                    @Override
                    public int compare(Usuario u1, Usuario u2) {
                        return Integer.valueOf(u2.getPuntos()).compareTo(Integer.valueOf(u1.getPuntos()));
                    }
                });

            } catch (IOException e) {
                existeUsuario = false;
                finish();
            } catch (XmlPullParserException e) {
                Log.d("ERROR", "Error: " + e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if (!existeUsuario)
                Toast.makeText(getApplicationContext(), "El usuario no existe en Comunio", Toast.LENGTH_SHORT).show();

            adaptador.notifyDataSetChanged();
        }

        public String calcularBeneficios(Integer puntos, Integer estrellas) {
            preferences = getSharedPreferences("MisPreferencias", MODE_PRIVATE);

            precioPunto = preferences.getInt("precioPunto", 0);
            precioEstrella = preferences.getInt("precioEstrella", 0);
            beneficioPuntos = puntos * precioPunto;
            beneficioEstrellas = estrellas * precioEstrella;

            if(puntos <= 0)
                beneficioTotal = beneficioEstrellas;
            else
                beneficioTotal = beneficioPuntos + beneficioEstrellas;

            return beneficioTotal.toString();
        }
    }
}