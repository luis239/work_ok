package com.immagine.workok.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.immagine.workok.Constants;
import com.immagine.workok.R;
import com.immagine.workok.model.Project;
import com.immagine.workok.model.Task;
import com.immagine.workok.model.User;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.immagine.workok.R.id.reciclador;

public class NewTaskActivity extends AppCompatActivity {


    private int projectId;
    private CreateTask mTask = null;
    private UpdateTask mTaskU = null;
    private EditText nameTask;
    private EditText description;
    private RadioButton inProgress;
    private RadioButton rejected;
    private RadioButton done;
    private EditText percent;
    private int status_id = 3;
    private Task task;
    private int isEdit = 0;
    private RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        nameTask = (EditText)findViewById(R.id.projectName);
        description = (EditText)findViewById(R.id.observaciones);
        percent = (EditText)findViewById(R.id.percent);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        inProgress = (RadioButton)findViewById(R.id.radioButton);
        inProgress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (inProgress.isChecked())
                    status_id = 3;
            }
        });

        rejected = (RadioButton)findViewById(R.id.radioButton2);
        rejected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rejected.isChecked())
                    status_id = 4;
            }
        });
        done = (RadioButton)findViewById(R.id.radioButton3);
        done.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (done.isChecked())
                    status_id = 5;
            }
        });

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEdit == 0)
                    createTask();
                else
                    updateTask();
            }
        });

        Bundle bundle = getIntent().getExtras();
        isEdit = bundle.getInt("edit");
        if(isEdit == 0)
            projectId = bundle.getInt("ID");
        else {
            Intent intent = getIntent();
            task = (Task) intent.getSerializableExtra("task");
            nameTask.setText(task.getTitle());
            status_id = task.getStatus_id();
            if(status_id==3) {
                inProgress.setChecked(true);

            }else if(status_id==4){
                rejected.setChecked(true);
            }else if(status_id==5){
                done.setChecked(true);
            }
            description.setText(task.getDescription());
            percent.setText(String.valueOf(task.getPercentage()));
            setTitle("Actualizar Tarea");


        }

    }

    private void updateTask() {

        int percent = Integer.parseInt(this.percent.getText().toString());
        mTaskU = new UpdateTask(nameTask.getText().toString(),description.getText().toString(),"09/06/2016","20/06/20160",projectId,status_id,
                percent,User.user.getUser_id(),NewTaskActivity.this,task.getTask_id());

        mTaskU.execute((Void) null);
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

    private void createTask() {

        int percent = Integer.parseInt(this.percent.getText().toString());
        mTask = new CreateTask(nameTask.getText().toString(),description.getText().toString(),"09/06/2016","20/06/20160",projectId,status_id,
                User.user.getUser_id(),percent ,NewTaskActivity.this);
        mTask.execute((Void) null);

    }

    public class CreateTask extends AsyncTask<Void, Void, Boolean> {

        String nameTask;
        String description;
        String dateStart;
        String dateEnd;
        int userId,project_id,status_id,percentage;
        ProgressDialog progressDialog;

        public CreateTask(String nameProject, String description, String dateStart, String dateEnd, int project_id,int status_id,int percentage , int user_id, Activity a) {
            this.nameTask = nameProject;
            this.description = description;
            this.dateStart = dateStart;
            this.dateEnd = dateEnd;
            this.userId = user_id;
            this.project_id = project_id;
            this.status_id = status_id;
            this.percentage = percentage;
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
            String dataUrl = "http://www.jexsantofagasta.cl/workok/wotask.php";
            String dataUrlParameters = "title="+nameTask+"&description="+description+"&date_start="+dateStart+"&date_end="+dateEnd
                    +"&status_id="+status_id+"&project_id="+projectId+"&user_id="+userId+"&action="+ Constants.ACTION_CREATE;
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
                    Log.d("Server response", responseStr);
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

            if (success) {
                if (success) {

                    Toast.makeText(NewTaskActivity.this,"Tarea Creada Exitosamente",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    finish();

                }
            } else {
                Toast.makeText(NewTaskActivity.this,"Error al crear la Tarea",Toast.LENGTH_LONG).show();
            }
            progressDialog.dismiss();
        }

        @Override
        protected void onCancelled() {
            mTask = null;
        }
    }


    public class UpdateTask extends AsyncTask<Void, Void, Boolean> {

        String nameTask;
        String description;
        String dateStart;
        String dateEnd;
        int userId,project_id,status_id,percentage,task_id;
        ProgressDialog progressDialog;

        public UpdateTask(String nameProject, String description, String dateStart, String dateEnd, int project_id,
                          int status_id,int percentage , int user_id, Activity a,int task_id) {
            this.nameTask = nameProject;
            this.description = description;
            this.dateStart = dateStart;
            this.dateEnd = dateEnd;
            this.userId = user_id;
            this.project_id = project_id;
            this.status_id = status_id;
            this.percentage = percentage;
            this.task_id = task_id;
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
            String dataUrl = "http://www.jexsantofagasta.cl/workok/wotask.php";
            String dataUrlParameters = "title="+nameTask+"&description="+description+"&date_start="+dateStart+"&date_end="+dateEnd
                    +"&status_id="+status_id+"&project_id="+projectId+"&user_id="+userId+"&task_id="+task_id+"&percentage="+percentage+"&action="+ Constants.ACTION_UPDATE;
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
                    Log.d("Server response", responseStr);
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

            if (success) {
                if (success) {

                    Toast.makeText(NewTaskActivity.this,"Tarea Actualizada Exitosamente",Toast.LENGTH_LONG).show();

                    finish();
                    progressDialog.dismiss();

                }
            } else {
                Toast.makeText(NewTaskActivity.this,"Error al Actualizar la Tarea",Toast.LENGTH_LONG).show();
            }
            progressDialog.dismiss();
        }

        @Override
        protected void onCancelled() {
            mTask = null;
        }
    }


}
