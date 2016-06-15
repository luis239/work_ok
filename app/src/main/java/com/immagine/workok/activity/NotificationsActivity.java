package com.immagine.workok.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.immagine.workok.Constants;
import com.immagine.workok.R;
import com.immagine.workok.adapter.NotificationsAdapter;
import com.immagine.workok.adapter.ProjectAdapter;
import com.immagine.workok.model.Notifications;
import com.immagine.workok.model.Project;
import com.immagine.workok.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.immagine.workok.R.id.reciclador;

public class NotificationsActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private TextView message;
    private NotificationListTask mTask = null;
    private List<Notifications> items = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        message = (TextView) findViewById(R.id.message);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        recycler = (RecyclerView) findViewById(reciclador);
        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getNotifications();
    }

    private void getNotifications() {

        mTask = new NotificationListTask(this);
        mTask.execute((Void) null);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
            default:
                break;
        }

        return true;
    }

    public class NotificationListTask extends AsyncTask<Void, Void, Boolean> {

        ProgressDialog progressDialog;

        NotificationListTask(Activity a) {
            progressDialog = new ProgressDialog(a,R.style.AppTheme_Dark_Dialog);


        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Cargando...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.



            String dataUrl = "http://www.jexsantofagasta.cl/workok/wouser.php";
            String dataUrlParameters = "user_id="+ User.user.getUser_id()+"&action=9";
            URL url;
            HttpURLConnection connection = null;
            try {
                // Create connection
                url = new URL(dataUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                connection.setRequestProperty("Content-Length","" + Integer.toString(dataUrlParameters.getBytes().length));
                connection.setRequestProperty("Content-Language", "en-US");
                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                // Send request
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
                if(jsonObj.getString("success").equals("1")){
                    JSONArray dataArray = jsonObj.getJSONArray("data");
                    items.clear();
                    for (int i = 0;i<dataArray.length();i++) {

                        JSONObject data = dataArray.getJSONObject(i);
                        Notifications notifications = new Notifications(data.getString("title"),data.getString("content"));
                        items.add(notifications);
                    }
                    Log.d("Server response",responseStr);
                    return true;
                }else{


                    return false;
                }


            } catch (Exception e) {

                e.printStackTrace();
                return false;

            } finally {

                if (connection != null) {
                    connection.disconnect();
                }
            }

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mTask = null;
            progressDialog.dismiss();
            //showProgress(false);
            if (success) {

                if (!items.isEmpty())
                    message.setVisibility(View.GONE);
                adapter = new NotificationsAdapter(items);
                recycler.setAdapter(adapter);



            }else{

            }

        }
    }
}
