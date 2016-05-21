package com.proyectofinal.saul.comunioadmin;

import android.os.AsyncTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by Saul on 07/05/2016.
 */
public class MyAsyncTask extends AsyncTask<String, Integer, String> {
    String NAMESPACE = "http://rpc.comunio.es/soapservice.php";
    String URL="http://rpc.comunio.es/soapservice.php/getuserid";
    String METHOD_NAME = "getuserid";
    String SOAP_ACTION = "http://rpc.comunio.es/soapservice.php/";
    SoapObject request;
    SoapSerializationEnvelope envelope;
    HttpTransportSE transporte;

    @Override
    protected void onPreExecute() {
        request = new SoapObject(NAMESPACE, METHOD_NAME);
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        transporte = new HttpTransportSE(URL);
    }

    @Override
        protected String doInBackground(String... params) {
        request.addProperty("USUARIO", params.toString());

        //envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        try
        {
            transporte.call(SOAP_ACTION, envelope);

            SoapPrimitive resultado_xml =(SoapPrimitive)envelope.getResponse();
            String res = resultado_xml.toString();

            if(res.equals("1"))
                return res;
        }
        catch (Exception e)
        {
            return "Error!" + e.getMessage();
        }

        return "Entra en el último return";
    }
}
