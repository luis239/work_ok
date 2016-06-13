package com.immagine.workok.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.immagine.workok.R;
import com.immagine.workok.activity.MainActivity;

/**
 * Created by lfagundez on 12/6/16.
 */
public class GCMListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Cuando el mensage es recibido.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from,data);
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        /**
         * El  mensaje recibido se proceso en este metodo.
         * Ejemplo: - Sincronizar con servidor.
         *     - Almacenar mensajes en base de datos local.
         *     - Actualizar UI.
         *     - Mostrar nofificaciones
         */

        //En este caso mostraremos una notificacion
        this.MostrarNotification(message);
    }


    private void MostrarNotification(String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Work Ok")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int idNotificacion = 1;
        notificationManager.notify(1, notificationBuilder.build());
    }
}
