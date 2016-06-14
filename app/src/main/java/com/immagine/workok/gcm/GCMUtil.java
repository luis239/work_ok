package com.immagine.workok.gcm;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.immagine.workok.PreferencesUtil;
import com.immagine.workok.model.User;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lfagundez on 13/6/16.
 */
public class GCMUtil {

    static String SENDER_ID = "926901106879";

    public static String ObtenerRegistrationTokenEnGcm(Context context) throws  Exception
    {
        InstanceID instanceID = InstanceID.getInstance(context);
        String token = instanceID.getToken(SENDER_ID,
                GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

        return token;
    }

    public static Boolean RegistrarseEnAplicacionServidor(Context context,String registrationToken) throws  Exception {
        int idUser = User.user.getUser_id();
        String dataUrl = "http://www.jexsantofagasta.cl/workok/wouser.php";
        String dataUrlParameters = "user_id=" + idUser + "&token=" + registrationToken + "&action=8";
        URL url = new URL(dataUrl);
        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000 /* milliseconds */);
            connection.setConnectTimeout(15000 /* milliseconds */);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(dataUrlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.writeBytes(dataUrlParameters);
            wr.flush();
            wr.close();
            // Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();

            String responseStr = response.toString();
            JSONObject jsonObj = new JSONObject(responseStr);
            if (jsonObj.getString("success").equals("1")) {

                return true;
            } else {

                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {

            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static Boolean UpdateRegisterServer(Context context, String registrationToken, int tokenUserId) throws  Exception {
        int idUser = tokenUserId;
        String dataUrl = "http://www.jexsantofagasta.cl/workok/wouser.php";
        String dataUrlParameters = "user_id=" + idUser + "&token=" + registrationToken + "&action=10";

        URL url = new URL(dataUrl);
        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000 /* milliseconds */);
            connection.setConnectTimeout(15000 /* milliseconds */);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(dataUrlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.writeBytes(dataUrlParameters);
            wr.flush();
            wr.close();
            // Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();

            String responseStr = response.toString();
            JSONObject jsonObj = new JSONObject(responseStr);
            if (jsonObj.getString("success").equals("1")) {
                Log.d("Server response", responseStr);
                return true;
            } else {

                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {

            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
