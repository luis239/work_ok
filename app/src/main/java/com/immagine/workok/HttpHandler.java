package com.immagine.workok;

/**
 * Created by Alejandro on 09/06/2016.
 */
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.message.BasicNameValuePair;

import org.json.JSONObject;




@SuppressWarnings("deprecation")
public class HttpHandler {

    private String server_url_recibir_proyecto = "http://www.jexsantofagasta.cl/workok/woproject.php";


    //////////////extraer eventos de la bd
    public String Recibir_Proyecto(String project_id) {


        try {

            String action = "10";

            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost(server_url_recibir_proyecto);


            List<NameValuePair> param = new ArrayList<NameValuePair>();

            param.add(new BasicNameValuePair("project_id", project_id));
            param.add(new BasicNameValuePair("action", action));

            httppost.setEntity(new UrlEncodedFormEntity(param));

            HttpResponse httpresponse = httpclient.execute(httppost);
            HttpEntity entity = httpresponse.getEntity();

            String result = EntityUtils.toString(entity);

            return result;


        } catch (Exception e) {
            return "No internet connection";
        }


    }
}