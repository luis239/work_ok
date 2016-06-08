package com.immagine.workok.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.immagine.workok.R;

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
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        container = (LinearLayout) findViewById(R.id.container);
        startDate = (EditText) findViewById(R.id.startDate);
        endDate = (EditText) findViewById(R.id.endDate);
        name = (EditText) findViewById(R.id.projectName);
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




    }


    public void validateFields(){

        // Reset errors.
        this.name.setError(null);
        endDate.setError(null);
        startDate.setError(null);
        description.setError(null);


        String desc = description.getText().toString();
        String name = this.name.getText().toString();

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

            startDate.setError("Fecha inicial no puede estar despues de la fecha final");
            focusView = startDate;
            Snackbar.make(container, "Fecha inicial no puede estar despues de la fecha final", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            cancel = true;
        }

        if(initDate.equals(finishDate)){

            startDate.setError("Fecha inicial no puede estar despues de la fecha final");
            focusView = startDate;
            Snackbar.make(container, "Las Fechas no pueden ser iguales", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //createProject(name,email, password);
            Log.d("Tag","Pressed");

        }
    }

    public void createProject(String nameProject,String description,String dateStart,String dateEnd,int userId){



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

            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                // Use the current date as the default date in the picker
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                return new DatePickerDialog(getActivity(), this, year, month, day);
            }

            public void onDateSet(DatePicker view, int year, int month, int day) {

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

                startDate.setText(day + "/" + (month + 1) + "/" + year);
                try {
                    initDate = sdf.parse(startDate.getText().toString());
                    initDate.setTime(0);
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

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            endDate.setText(day + "/" + (month + 1) + "/" + year);
            try {
                finishDate = sdf.parse(endDate.getText().toString());
                finishDate.setTime(0);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }


    }
}
