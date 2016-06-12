package com.immagine.workok.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.immagine.workok.Constants;
import com.immagine.workok.PreferencesUtil;
import com.immagine.workok.R;
import com.immagine.workok.adapter.UserProjectAdapter;
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

public class AddUserProject extends AppCompatActivity implements UserProjectAdapter.OnItemClickListener,UserSelectionDialog.OnClickAddUser {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private Button addButton;
    private AlertDialog.Builder alertDialog;
    private AlertDialog dialog;
    private List<User> items = new ArrayList<>();
    private UserListInProjectTask mTask = null;
    private int projectId = 0;
    private DeletUserProjectTask dTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_project);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Bundle bundle = getIntent().getExtras();
        projectId = bundle.getInt("ID_PROJECT");
        addButton = (Button) findViewById(R.id.addUser);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogAddUser();
            }
        });

/*        items.add(new User("Persona 1","luis", 2));
        items.add(new User("Persona 2","jumi", 3));
        items.add(new User("Persona 3","user1", 1));
        items.add(new User("Persona 4","user2",8));
        items.add(new User("Persona 5","peter", 5));*/

        recycler = (RecyclerView) findViewById(R.id.listView);
        //recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

  /*      // Crear un nuevo adaptador
        adapter = new UserProjectAdapter(items,this);
        recycler.setAdapter(adapter);*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUsersList();
        PreferencesUtil preference = new PreferencesUtil(this);
        User.user.setUser_id(preference.getUserId());
        User.user.setFullname(preference.getUserName());

    }

    private void getUsersList() {

                        mTask = new UserListInProjectTask(this);
                        mTask.execute((Void) null);


    }

    private void dialogAddUser() {
        UserSelectionDialog cd = new UserSelectionDialog(this,this,projectId);
        cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cd.show();

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

    public List<User> getListUsers(){

        return items;
    }

    @Override
    public void onClick(UserProjectAdapter.UserProjectViewHolder holder, final int index) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
// Add the buttons
        builder.setMessage("Desea eliminar este usuario?");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dTask = new DeletUserProjectTask(AddUserProject.this,items.get(index).getUser_id(),index);
                dTask.execute((Void)null);

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        AlertDialog dialog = builder.show();
        dialog.setCanceledOnTouchOutside(true);

    }

    @Override
    public void onClickAdd(List<User> itemsSelected) {

        getUsersList();
       /* int pos = items.size();
        items.addAll(itemsSelected);
        adapter.notifyItemInserted(pos);*/
    }


    public class UserListInProjectTask extends AsyncTask<Void, Void, Boolean> {


        ProgressDialog progressDialog;
        UserListInProjectTask(Activity activity) {

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



            String dataUrl = "http://www.jexsantofagasta.cl/workok/woproject.php";
            String dataUrlParameters = "action=7&project_id="+projectId;
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

            //showProgress(false);
            if (success) {

                adapter = new UserProjectAdapter(items,AddUserProject.this);
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

    public class DeletUserProjectTask extends AsyncTask<Void, Void, Boolean> {


        int userID,index;
        ProgressDialog progressDialog;
        DeletUserProjectTask(Activity activity,int userID,int index) {

            this.userID = userID;
            this.index = index;
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



            String dataUrl = "http://www.jexsantofagasta.cl/workok/woproject.php?action="+ Constants.ACTION_DELETE_USER_FROM_PROJECT;
            String dataUrlParameters = "users="+userID+"&project_id="+projectId;
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

            //showProgress(false);
            if (success) {

                adapter = new UserProjectAdapter(items,AddUserProject.this);
                recycler.setAdapter(adapter);
                items.remove(index);
                adapter.notifyItemRemoved(index);


            }
            progressDialog.dismiss();
        }

        @Override
        protected void onCancelled() {
            mTask = null;
            //showProgress(false);
        }
    }
}
