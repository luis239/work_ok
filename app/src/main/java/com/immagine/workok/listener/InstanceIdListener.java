package com.immagine.workok.listener;

import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.iid.InstanceIDListenerService;
import com.immagine.workok.gcm.GCMUtil;

/**
 * Created by lfagundez on 12/6/16.
 */
public class InstanceIdListener extends InstanceIDListenerService {

    /**
     * Se llama cuando Gcm servers actualizan el registration token, principalemnte por motivos  de seguridad
     */
    @Override
    public void onTokenRefresh() {
        //obtener nuevamente el token y enviarlo a la aplicacion servidor
        RegitroGcmcAsyncTask regitroGcmcAsyncTask = new RegitroGcmcAsyncTask();
        regitroGcmcAsyncTask.execute();
    }

    private class RegitroGcmcAsyncTask extends AsyncTask<String , String, Object>
    {

        @Override
        protected Object doInBackground(String ... params) {

            try {

                publishProgress("Obteniendo Token en GCM Servers...");
                String registrationToken = GCMUtil.ObtenerRegistrationTokenEnGcm(getApplicationContext());

                publishProgress("Enviando Registro...");
                Boolean respuesta = GCMUtil.RegistrarseEnAplicacionServidor(getApplicationContext(),registrationToken);
                return respuesta;
            }
            catch (Exception ex){
                return ex;
            }
        }

        protected void onProgressUpdate(String... progress) {
            Toast.makeText(getApplicationContext(),progress[0],Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Object result)
        {
            if(result instanceof  String)
            {
                String resulatado = (String)result;
                Toast.makeText(getApplicationContext(), "Registro exitoso. " + resulatado, Toast.LENGTH_SHORT).show();
            }
            else if (result instanceof Exception)//Si el resultado es una Excepcion..hay error
            {
                Exception ex = (Exception) result;
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }
}
