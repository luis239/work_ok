package com.immagine.workok.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.getbase.floatingactionbutton.FloatingActionButton;

import com.immagine.workok.Constants;
import com.immagine.workok.PreferencesUtil;
import com.immagine.workok.R;
import com.immagine.workok.adapter.ProjectAdapter;
import com.immagine.workok.adapter.TaskProjectAdapter;
import com.immagine.workok.adapter.UserProjectAdapter;
import com.immagine.workok.model.Project;
import com.immagine.workok.model.Task;
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

public class ProyectDetailsActivity extends AppCompatActivity implements TaskProjectAdapter.OnItemClickListener {


    private Activity act;
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private List<Task> items = new ArrayList<>();
    private FloatingActionButton addTask;
    private FloatingActionButton addUser;
    private FloatingActionButton editProyect;
    private TaskListTask mTask = null;
    private DeleteTask dTask = null;
    private Project project;
    private TextView message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proyect_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent = getIntent();
        project = (Project) intent.getSerializableExtra("project");


        TextView title = (TextView) findViewById(R.id.project_title);
        title.setText(project.getTitle());
        recycler = (RecyclerView) findViewById(R.id.reciclador);
        //recycler.setHasFixedSize(true);
        message = (TextView) findViewById(R.id.message);
        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(act);
        recycler.setLayoutManager(lManager);


        addTask = (FloatingActionButton) findViewById(R.id.add_task_fab);
        addUser = (FloatingActionButton) findViewById(R.id.add_user_fab);
        editProyect = (FloatingActionButton) findViewById(R.id.edit_proyect_fab);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("ID",project.getProject_id());
                bundle.putInt("edit",0);
                Intent intent =  new Intent(ProyectDetailsActivity.this,NewTaskActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("ID_PROJECT",project.getProject_id());
                Intent intent =  new Intent(ProyectDetailsActivity.this,AddUserProject.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        editProyect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(ProyectDetailsActivity.this,NewProjectActivity.class);
                intent.putExtra("project",project);
                Bundle bundle = new Bundle();
                bundle.putInt("edit",1);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


    }



    @Override
    protected void onResume() {
        super.onResume();
        PreferencesUtil preference = new PreferencesUtil(this);
        User.user.setUser_id(preference.getUserId());
        User.user.setFullname(preference.getUserName());
        getTasksListTask(project.getProject_id());

    }

    private void getTasksListTask(final int project_id) {


        mTask = new TaskListTask(project_id,this);
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
    public void onClick(View view, final int pos) {


        Intent intent;
        if (view.getId() ==R.id.imageButton) {
            int projectId = items.get(pos).getProject_id();
            intent = new Intent(this, GantterActivity.class);
            intent.putExtra("project_id", projectId);
            startActivity(intent);
        }
        if(view.getId() == R.id.delete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Desea eliminar esta Tarea?");
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dTask = new DeleteTask(ProyectDetailsActivity.this,items.get(pos).getTask_id(),pos);
                    dTask.execute((Void) null);
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
        if(view.getId() ==R.id.card_view) {
            Task task = items.get(pos);
            intent = new Intent(this,NewTaskActivity.class);
            intent.putExtra("task",task);
            intent.putExtra("come_from_details",true);
            Bundle bundle = new Bundle();
            bundle.putInt("edit",1);
            bundle.putInt("ID",project.getProject_id());
            intent.putExtras(bundle);
            startActivity(intent);
        }




    }

    public class TaskListTask extends AsyncTask<Void, Void, Boolean> {

        private final int projectId;

        ProgressDialog progressDialog;
        TaskListTask(int project_id,Activity a) {
            projectId = project_id;
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



            String dataUrl = "http://www.jexsantofagasta.cl/workok/woproject.php";
            String dataUrlParameters = "project_id="+projectId+"&action="+ Constants.ACTION_LIST_TASK;
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
                        Task task = new Task(data.getString("title"),
                                data.getString("description"),data.getString("date_start"),data.getString("date_end"),data.getInt("percentage"),
                                data.getInt("status_id"),data.getInt("project_id"),data.getInt("user_id"),data.getInt("task_id"),data.getString("fullname"));
                        items.add(task);
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
                if(!items.isEmpty())
                    message.setVisibility(View.GONE);
                adapter = new TaskProjectAdapter(items,ProyectDetailsActivity.this);
                recycler.setAdapter(adapter);

            }else{

                /*if(items.isEmpty())
                    message.setVisibility(View.VISIBLE);*/
            }
            progressDialog.dismiss();
        }

        @Override
        protected void onCancelled() {
            mTask = null;
            //showProgress(false);
        }
    }

    public class DeleteTask extends AsyncTask<Void, Void, Boolean> {


        int taskId,index;
        ProgressDialog progressDialog;
        DeleteTask(Activity activity,int taskId,int index) {

            this.taskId = taskId;
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
            String dataUrl = "http://www.jexsantofagasta.cl/workok/wotask.php";
            String dataUrlParameters = "task_id="+taskId+"&action="+ Constants.ACTION_DELETE;
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


                recycler.setAdapter(adapter);
                items.remove(index);
                adapter.notifyItemRemoved(index);
                if(items.isEmpty())
                    message.setVisibility(View.VISIBLE);
                Toast.makeText(ProyectDetailsActivity.this,"Tarea Eliminada satisfactoriamente",Toast.LENGTH_LONG).show();


            }else{

                Toast.makeText(ProyectDetailsActivity.this,"Error Eliminando la tarea",Toast.LENGTH_LONG).show();
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
