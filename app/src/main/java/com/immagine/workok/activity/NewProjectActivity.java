package com.immagine.workok.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.immagine.workok.Constants;
import com.immagine.workok.PreferencesUtil;
import com.immagine.workok.R;
import com.immagine.workok.model.Project;
import com.immagine.workok.model.User;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewProjectActivity extends AppCompatActivity {



    static EditText startDate;
    static EditText endDate;
    static Date initDate = new Date(),finishDate = new Date();
    private Button createProject;
    private EditText name;
    private EditText description;
    private LinearLayout container;
    private CreateProjectTask mTask = null;
    private Project project;
    private static int isEdit;
    private UpdateProjectTask uTask = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        container = (LinearLayout) findViewById(R.id.container);
        startDate = (EditText) findViewById(R.id.startDate);
        endDate = (EditText) findViewById(R.id.endDate);
        name = (EditText) findViewById(R.id.assigned_by);
        description = (EditText) findViewById( R.id.description);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDatePickerDialog(v);

            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                endDatePickerDialog(v);
            }
        });
        createProject = (Button) findViewById(R.id.create_project);
        createProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateFields();
            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle!= null){

            isEdit = bundle.getInt("edit");
        }
        if (isEdit == 1) {
            project = (Project) intent.getSerializableExtra("project");
            name.setText(project.getTitle());
            description.setText(project.getDescription());
            String dateInit = inverseDate(project.getDateStart());
            String dateEnd = inverseDate(project.getDateEnd());
            startDate.setText(dateInit);
            endDate.setText(dateEnd);
            createProject.setText("Editar Proyecto");
            setTitle("Editar Proyecto");
            initializeDates();
        }

    }

    private void initializeDates() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {
            finishDate = sdf.parse(endDate.getText().toString());
            initDate = sdf.parse(startDate.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private String inverseDate(String string) {

        String[] parts = string.split("-");
        String year = parts[0];
        String month = parts[1];
        String day = parts[2];
        String finalDate= day+"/"+month+"/"+year;

        return finalDate ;

    }

        @Override
        protected void onResume() {
            super.onResume();
            PreferencesUtil preference = new PreferencesUtil(this);
            User.user.setUser_id(preference.getUserId());
            User.user.setFullname(preference.getUserName());
        }

    public void validateFields(){

        // Reset errors.
        this.name.setError(null);
        endDate.setError(null);
        startDate.setError(null);
        description.setError(null);


        final String desc = description.getText().toString();
        final String name = this.name.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if(!isNameProjectValid(name)){

            this.name.setError(getString(R.string.error_username_short));
            focusView = this.name;
            cancel = true;
        }

        if (desc.length() == 0){

            description.setError(getString(R.string.error_field_required));
            focusView = description;
            cancel = true;
        }

        if (endDate.getText().length() == 0 || startDate.getText().length() == 0){
            endDate.setError(getString(R.string.error_field_required));
            //focusView = endDate;
            cancel = true;
        }
        if(initDate.after(finishDate)){

            startDate.setError("");
            focusView = startDate;
            Snackbar.make(container, "Fecha inicial no puede estar despues de la fecha final", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            cancel = true;
        }

        if(initDate.equals(finishDate)){

            startDate.setError("");
            focusView = startDate;
            Snackbar.make(container, "Las Fechas no pueden ser iguales", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else if (isEdit == 0) {
            mTask = new CreateProjectTask(name, desc, startDate.getText().toString(), endDate.getText().toString(), User.user.getUser_id(),this);
            mTask.execute((Void) null);
        }else{

            updateProject();
            Log.d("Tag","Pressed");

        }
    }

    private void updateProject() {
        uTask = new UpdateProjectTask(name.getText().toString(), description.getText().toString(), startDate.getText().toString(), endDate.getText().toString(),project.getProject_id(),this);
        uTask.execute((Void) null);
    }

    public class CreateProjectTask extends AsyncTask<Void, Void, Boolean> {

        String nameProject;
        String description;
        String dateStart;
        String dateEnd;
        int userId;
        ProgressDialog progressDialog;
        public CreateProjectTask(String nameProject, String description, String dateStart, String dateEnd, int userId,Activity a) {
            this.nameProject = nameProject;
            this.description = description;
            this.dateStart = dateStart;
            this.dateEnd = dateEnd;
            this.userId = userId;
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
            String dataUrl = "http://www.jexsantofagasta.cl/workok/woproject.php";
            try {
                nameProject =  URLEncoder.encode(nameProject, "UTF-8");
                description = URLEncoder.encode(description, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String dataUrlParameters = "title="+nameProject+"&description="+description+"&date_start="+dateStart+"&date_end="+dateEnd
                    +"&user_id="+userId+"&action="+ Constants.ACTION_CREATE;
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

            progressDialog.dismiss();
            if (success) {
                if (success) {

                    Toast.makeText(NewProjectActivity.this,"Proyecto Creado Exitosamente",Toast.LENGTH_LONG).show();

                    finish();

                }
            } else {
                Toast.makeText(NewProjectActivity.this,"Error",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            mTask = null;
        }
    }


    public class UpdateProjectTask extends AsyncTask<Void, Void, Boolean> {

        String nameProject;
        String description;
        String dateStart;
        String dateEnd;
        int userId,project_id,status_id,percentage,task_id;
        ProgressDialog progressDialog;

        public UpdateProjectTask(String name, String description, String dateStart, String dateEnd,int project_id,Activity a) {
            this.nameProject = name;
            this.description = description;
            this.dateStart = dateStart;
            this.dateEnd = dateEnd;
            this.project_id = project_id;
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
            String dataUrl = "http://www.jexsantofagasta.cl/workok/woproject.php";
            try {
                nameProject =  URLEncoder.encode(nameProject, "UTF-8");
                description = URLEncoder.encode(description, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String dataUrlParameters = "title="+nameProject+"&description="+description+"&date_start="+dateStart+"&date_end="+dateEnd
                    +"&status_id="+status_id+"&project_id="+project_id+"&user_id="+userId+"&task_id="+task_id+"&percentage="+percentage+"&action="+ Constants.ACTION_UPDATE;
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

                    Toast.makeText(NewProjectActivity.this,"Proyecto Actualizado Exitosamente",Toast.LENGTH_LONG).show();

                    finish();
                    progressDialog.dismiss();

                }
            } else {
                Toast.makeText(NewProjectActivity.this,"Error al Actualizar el proyecto",Toast.LENGTH_LONG).show();
            }
            progressDialog.dismiss();
        }

        @Override
        protected void onCancelled() {
            mTask = null;
        }
    }




    public boolean isNameProjectValid(String name){
        if (name.length()>4)
            return true;
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        else
            return false;


        // return super.onOptionsItemSelected(item);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {


            final Calendar c = Calendar.getInstance();
            int  year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            if(isEdit == 1) {
                try {
                    initDate = sdf.parse(startDate.getText().toString());
                    c.setTime(initDate);
                    year = c.get(Calendar.YEAR);
                    month = c.get(Calendar.MONTH);
                    day = c.get(Calendar.DAY_OF_MONTH);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {


            startDate.setText(day + "/" + (month + 1) + "/" + year);
            try {
                initDate = sdf.parse(startDate.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


    }

    public void endDatePickerDialog(View v) {
        DialogFragment newFragment = new endDatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class endDatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int  year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            if(isEdit == 1) {
                try {
                    finishDate = sdf.parse(endDate.getText().toString());
                    c.setTime(finishDate);
                    year = c.get(Calendar.YEAR);
                    month = c.get(Calendar.MONTH);
                    day = c.get(Calendar.DAY_OF_MONTH);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {

            endDate.setText(day + "/" + (month + 1) + "/" + year);
            try {
                finishDate = sdf.parse(endDate.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }


    }
}
