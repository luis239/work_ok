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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.immagine.workok.Constants;
import com.immagine.workok.PreferencesUtil;
import com.immagine.workok.R;
import com.immagine.workok.model.Project;
import com.immagine.workok.model.Task;
import com.immagine.workok.model.User;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NewTaskActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private int projectId;
    private CreateTask mTask = null;
    private UpdateTask mTaskU = null;
    private UserListInProjectTask mTask2 = null;
    private EditText nameTask;
    private EditText description;
    private RadioButton inProgress;
    private RadioButton rejected;
    private RadioButton done;
    private EditText percent;
    private int status_id = 3;
    private Task task;
    private static int isEdit = 0;
    private ArrayList<User> items = new ArrayList<>();
    private int userIdSelected = 0;
    private Spinner spinner;
    static TextView label;
    static EditText startDate;
    static EditText endDate;
    static Date initDate = new Date(),finishDate = new Date();
    private LinearLayout container;
    ArrayAdapter<User> adapter;
    private boolean isOwner = true;
    private Button add;
    private LinearLayout spinnerLayout;
    private Project project;
    private Date projectFinishDate = new Date();
    private Date projectInitDate = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        Intent intent = getIntent();
        project = (Project) intent.getSerializableExtra("project");
        boolean comeFrom = false;
        spinnerLayout = (LinearLayout) findViewById( (R.id.layoutSpiner)) ;
        container = (LinearLayout) findViewById(R.id.container_dates) ;
        nameTask = (EditText)findViewById(R.id.assigned_by);
        description = (EditText)findViewById(R.id.observaciones);
        percent = (EditText)findViewById(R.id.percent);
        percent.setText("0");
        add = (Button) findViewById(R.id.add);
        String pDateEnd,pDateStart;
        pDateEnd = inverseDate(project.getDateEnd());
        pDateStart = inverseDate(project.getDateStart());
        dateCasting(pDateStart,pDateEnd);

        inProgress = (RadioButton)findViewById(R.id.radioButton);
        inProgress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (inProgress.isChecked()) {
                    status_id = 3;
                    percent.setClickable(true);
                    percent.setFocusableInTouchMode(true);
                }
            }
        });

        rejected = (RadioButton)findViewById(R.id.radioButton2);
        rejected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rejected.isChecked()) {
                    status_id = 4;
                    percent.setText("0");
                    percent.setClickable(false);
                    percent.setFocusableInTouchMode(false);
                }
            }
        });
        done = (RadioButton)findViewById(R.id.radioButton3);
        done.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (done.isChecked()) {
                    status_id = 5;
                    percent.setText("100");
                    percent.setClickable(false);
                    percent.setFocusableInTouchMode(false);
                }
            }
        });

        startDate = (EditText) findViewById(R.id.init_date);
        endDate = (EditText) findViewById(R.id.end_date);
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

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyData();
            }
        });
        spinner = (Spinner) findViewById(R.id.spinner_users);
        final Bundle bundle = getIntent().getExtras();
        isEdit = bundle.getInt("edit");
        projectId = bundle.getInt("ID");
        if(isEdit == 1) {

            task = (Task) intent.getSerializableExtra("task");
            comeFrom = intent.getBooleanExtra("come_from_details",false);
            if(task.getOwnerId() != User.user.getUser_id() && !comeFrom)
                isOwner = false;
            nameTask.setText(task.getTitle());
            projectId = task.getProject_id();
            status_id = task.getStatus_id();
            if(status_id==3) {
                inProgress.setChecked(true);

            }else if(status_id==4){
                rejected.setChecked(true);
            }else if(status_id==5){
                done.setChecked(true);
            }
            description.setText("");
            percent.setText(String.valueOf(task.getPercentage()));
            String dateInit = inverseDate(task.getDate_start());
            String dateEnd = inverseDate(task.getDate_end());
            startDate.setText(dateInit);
            endDate.setText(dateEnd);
            button.setText(getString(R.string.update_status));
            setTitle("Actualizar Tarea");
            initializeDates();
            if (task.getStatus_id() == 8){

                startDate.setClickable(false);
                startDate.setFocusableInTouchMode(false);
                startDate.setEnabled(false);
                endDate.setClickable(false);
                endDate.setFocusableInTouchMode(false);
                endDate.setEnabled(false);
                nameTask.setClickable(false);
                nameTask.setFocusableInTouchMode(false);
                nameTask.setEnabled(false);
                percent.setClickable(false);
                percent.setFocusableInTouchMode(false);
                percent.setEnabled(false);
                description.setClickable(false);
                description.setFocusableInTouchMode(false);
                description.setEnabled(false);
                spinner.setClickable(false);
                spinner.setClickable(false);
                spinner.setFocusableInTouchMode(false);
                button.setVisibility(View.GONE);
                inProgress.setClickable(false);
                inProgress.setClickable(false);
                inProgress.setEnabled(false);
                rejected.setClickable(false);
                rejected.setClickable(false);
                rejected.setEnabled(false);
                done.setClickable(false);
                done.setClickable(false);
                done.setEnabled(false);
                add.setVisibility(View.GONE);

            }

        }
        if (!isOwner){

            startDate.setClickable(false);
            startDate.setFocusableInTouchMode(false);
            startDate.setEnabled(false);
            endDate.setClickable(false);
            endDate.setFocusableInTouchMode(false);
            endDate.setEnabled(false);
            nameTask.setClickable(false);
            nameTask.setFocusableInTouchMode(false);
            nameTask.setEnabled(false);
            spinner.setClickable(false);
            spinner.setFocusableInTouchMode(false);

        }


        label = (TextView)findViewById(R.id.no_assigned);
        label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("ID_PROJECT",projectId);
                Intent intent =  new Intent(NewTaskActivity.this,AddUserProject.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("ID_PROJECT",projectId);
                Intent intent =  new Intent(NewTaskActivity.this,AddUserProject.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
        percent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.equals("100")) {
                    done.setChecked(true);
                    inProgress.setChecked(false);
                }
            }
        });

        spinner.setOnItemSelectedListener(this);

    }

    private void dateCasting(String dateStart, String dateEnd) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            projectFinishDate = sdf.parse(dateEnd);
            projectInitDate = sdf.parse(dateStart);
        } catch (ParseException e) {
            e.printStackTrace();
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

    private void verifyData() {

        endDate.setError(null);
        startDate.setError(null);
        description.setError(null);


        final String desc = description.getText().toString();

        boolean cancel = false;
        View focusView = null;


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

        if(initDate.after(projectFinishDate)||initDate.before(projectInitDate)){

            startDate.setError("");
            focusView = startDate;
            Snackbar.make(container, "La fecha debe estar dentro del rango del proyecto "+inverseDate(project.getDateStart())+" - "+inverseDate(project.getDateEnd()), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            cancel = true;
        }
        if(finishDate.after(projectFinishDate)){

            endDate.setError("");
            focusView = startDate;
            Snackbar.make(container, "La fecha debe estar dentro del rango del proyecto "+inverseDate(project.getDateStart())+" - "+inverseDate(project.getDateEnd()), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            cancel = true;
        }
        /*if(initDate.equals(finishDate)){

            startDate.setError("");
            focusView = startDate;
            Snackbar.make(container, "Las Fechas no pueden ser iguales", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            cancel = true;
        }*/
        if (percent.getText().toString().length() == 0) {
            percent.setError("Introduzca el Porcentaje");
            percent.requestFocus();
            cancel = true;
        }else if(Integer.parseInt(percent.getText().toString()) > 100){
            percent.setError("Porcentaje no puede ser mayor a 100");
            percent.requestFocus();
            cancel = true;
        }
        if(userIdSelected == 0){
            focusView = startDate;
            Snackbar.make(container, "Debe Asignar un Usuario", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        }else if(isEdit == 0)
            createTask();
         else
            updateTask();


    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferencesUtil preference = new PreferencesUtil(this);
        User.user.setUser_id(preference.getUserId());
        User.user.setFullname(preference.getUserName());
        getUsersInProject();

    }

    public  void getUsersInProject(){

        mTask2 = new UserListInProjectTask(this);
        mTask2.execute((Void) null);

    }

    private void updateTask() {

        int percent = Integer.parseInt(this.percent.getText().toString());
        mTaskU = new UpdateTask(nameTask.getText().toString(),description.getText().toString(),startDate.getText().toString(),endDate.getText().toString(),projectId,status_id,
                percent,userIdSelected,NewTaskActivity.this,task.getTask_id(),User.user.getUser_id());

        mTaskU.execute((Void) null);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        else
            return false;

    }

    private void createTask() {

        int percent = Integer.parseInt(this.percent.getText().toString());
        mTask = new CreateTask(nameTask.getText().toString(),description.getText().toString(),startDate.getText().toString(),endDate.getText().toString(),projectId,status_id,
                userIdSelected,percent ,NewTaskActivity.this);
        mTask.execute((Void) null);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (parent.getId() == R.id.spinner_users) {
            userIdSelected = ((User) parent.getItemAtPosition(position)).getUser_id();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {



    }

    public class CreateTask extends AsyncTask<Void, Void, Boolean> {

        String nameTask;
        String description;
        String dateStart;
        String dateEnd;
        int userId,project_id,status_id,percentage;
        ProgressDialog progressDialog;

        public CreateTask(String nameProject, String description, String dateStart, String dateEnd, int project_id,int status_id,int user_id,int percentage ,  Activity a) {
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
            try {
                nameTask =  URLEncoder.encode(nameTask, "UTF-8");
                description = URLEncoder.encode(description, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
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
        int userModifyId;

        public UpdateTask(String nameProject, String description, String dateStart, String dateEnd, int project_id,
                          int status_id,int percentage , int user_id, Activity a,int task_id,int userModifyId) {
            this.nameTask = nameProject;
            this.description = description;
            this.dateStart = dateStart;
            this.dateEnd = dateEnd;
            this.userId = user_id;
            this.project_id = project_id;
            this.status_id = status_id;
            this.percentage = percentage;
            this.task_id = task_id;
            this.userModifyId = userModifyId;
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

            try {
                nameTask =  URLEncoder.encode(nameTask, "UTF-8");
                description = URLEncoder.encode(description, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String dataUrlParameters = "title="+nameTask+"&description="+description+"&date_start="+dateStart+"&date_end="+dateEnd
                    +"&status_id="+status_id+"&project_id="+projectId+"&user_id="+userId+"&task_id="+task_id+
                    "&percentage="+percentage+"&action="+ Constants.ACTION_UPDATE+"&assigned_id="+userModifyId;
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
                    Log.d("Server",responseStr);
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


                adapter = new ArrayAdapter<User>(NewTaskActivity.this,android.R.layout.simple_spinner_item)
                {

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {

                        View v = super.getView(position, convertView, parent);
                        if (position == getCount()) {
                            ((TextView) v.findViewById(android.R.id.text1)).setText("");
                            ((TextView) v.findViewById(android.R.id.text1)).setHint(getString(R.string.add_user));
                        }
                        return v;
                    }

                   @Override
                    public int getCount() {
                        return super.getCount()-1;
                    }



                };
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                adapter.addAll(items);
                User u = new User();
                u.setFullname("Agregar Persona a Tarea");
                adapter.add(u);
                if(adapter.getCount()+1 > 1){

                    label.setVisibility(View.GONE);
                    if(spinner.getVisibility() == View.GONE)
                        spinner.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.VISIBLE);
                    spinner.setAdapter(adapter);
                    spinner.setSelection(adapter.getCount());
                }

                if (isEdit == 1) {
                    for (int i = 0;i<adapter.getCount()+1;i++ ) {

                        if(adapter.getItem(i).getUser_id() == task.getUser_id())
                            spinner.setSelection(i);

                    }
                }


            }else{
                spinnerLayout.setVisibility(View.GONE);
            }
            progressDialog.dismiss();
        }

        @Override
        protected void onCancelled() {
            mTask = null;
            //showProgress(false);
        }
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
