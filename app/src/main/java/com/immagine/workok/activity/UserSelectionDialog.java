package com.immagine.workok.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.immagine.workok.Constants;
import com.immagine.workok.R;
import com.immagine.workok.adapter.UserProjectAdapter;
import com.immagine.workok.model.Project;
import com.immagine.workok.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Alejandro on 04/06/2016.
 */
public class UserSelectionDialog extends Dialog implements UserProjectAdapter.OnItemClickListener,View.OnClickListener{


    private Activity act;
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private Button add;
    private ImageView imageButton;
    private List<User> items = new ArrayList<>();
    private OnClickAddUser listener;
    private List<User>userSelected = new ArrayList<>();
    private UserListTask mTask = null;
    private UploadUserTask mTask2 = null;
    private int projectId;

    public UserSelectionDialog(Activity a,OnClickAddUser listener,int projectId) {
        super(a);
        this.act = a;
        this.listener = listener;
        this.projectId = projectId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_user_selector);

        this.setTitle(R.string.select_add_user);


        recycler = (RecyclerView) findViewById(R.id.recycler_view);
        add = (Button) findViewById(R.id.button2);
        add.setOnClickListener(this);
        //recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(act);
        recycler.setLayoutManager(lManager);

        // Crear un nuevo adaptador
        adapter = new UserProjectAdapter(items,true,this);
        recycler.setAdapter(adapter);

        getUsersList();
    }


    private void getUsersList() {

                        mTask = new UserListTask(this.act);
                        mTask.execute((Void) null);

    }



    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        else
            return false;

    }
    @Override
    public void onClick(UserProjectAdapter.UserProjectViewHolder holder, int idPromocion) {

        if(holder.checkBox.isChecked())
            items.get(idPromocion).setSelected(true);
        else
            items.get(idPromocion).setSelected(false);

    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case (R.id.button2):{

                for (User u:items) {
                    if (u.isSelected())
                        userSelected.add(u);
                }
            }
        }
        if(!userSelected.isEmpty()) {
            uploadUsers(userSelected);
            listener.onClickAdd(userSelected);
            this.cancel();
        }else{
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
// Add the buttons
            builder.setMessage("Debe seleccionar al menos uno");
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            AlertDialog dialog = builder.show();
            dialog.setCanceledOnTouchOutside(true);
        }



    }

    public interface OnClickAddUser{

        void onClickAdd(List<User> items);
    }

    public class UserListTask extends AsyncTask<Void, Void, Boolean> {


        ProgressDialog progressDialog;
        UserListTask(Activity activity) {

            progressDialog = new ProgressDialog(activity,R.style.AppTheme_Dark_Dialog);
        }


        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Espere...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.


            String dataUrl = "http://www.jexsantofagasta.cl/workok/wouser.php";
            String dataUrlParameters = "action="+ Constants.ACTION_LIST_USER_NOT_IN_PROJECT+"&project_id="+projectId;
            //String dataUrl = "http://www.jexsantofagasta.cl/workok/wouser.php";
            //String dataUrlParameters = "&action="+ Constants.ACTION_LIST;
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
                        User user = new User(data.getString("username"),
                                data.getString("fullname"),data.getInt("user_id"));
                        items.add(user);
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
            if (success) {

                adapter = new UserProjectAdapter(items,true,UserSelectionDialog.this);
                recycler.setAdapter(adapter);

            }
            progressDialog.dismiss();
        }

        @Override
        protected void onCancelled() {
            mTask = null;
            //showProgress(false);
        }
    }

    private void uploadUsers(List<User> itemsSelected) {

        mTask2 = new UploadUserTask(itemsSelected,this.getContext(),projectId);
        mTask2.execute((Void) null);
    }
    public class UploadUserTask extends AsyncTask<Void, Void, Boolean> {

        ProgressDialog progressDialog;

        List<User> users;
        int project_id;
        UploadUserTask(List<User> users,Context activity,int project_id) {

            this.users = users;
            this.project_id = project_id;
            progressDialog = new ProgressDialog(activity,R.style.AppTheme_Dark_Dialog);
        }


        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Espere...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            JSONObject parameters = new JSONObject();

            String ids = "";
            for (int i = 0;i< users.size();i++){

                ids += users.get(i).getUser_id();
                if (i<users.size()-1){
                    ids+=",";
                }

            }
            try {
                parameters.put("users", ids);
                parameters.put("project_id",String.valueOf(project_id));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String dataUrl = "http://www.jexsantofagasta.cl/workok/woproject.php?action=8";
            String dataUrlParameters = "users="+ids+"&project_id="+project_id;
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
                    Log.d("Server response",responseStr);
                    return true;
                }else{

                    Log.d("Server response",responseStr);
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
        }

        @Override
        protected void onCancelled() {
            mTask = null;
            //showProgress(false);
        }
    }
}
