package com.immagine.workok.activity;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.immagine.workok.R;
import com.immagine.workok.adapter.ProjectAdapter;
import com.immagine.workok.adapter.UserProjectAdapter;
import com.immagine.workok.model.Project;

import java.util.ArrayList;
import java.util.List;

import static com.immagine.workok.R.id.reciclador;

public class AddUserProject extends AppCompatActivity {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private Button addButton;
    private AlertDialog.Builder alertDialog;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_project);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        addButton = (Button) findViewById(R.id.addUser);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogAddUser();
            }
        });
        List<Project> items = new ArrayList<>();

        items.add(new Project("Persona 1", "Angel Beats", 20));
        items.add(new Project("Persona 2", "Death Note", 30));
        items.add(new Project("Persona 3", "Fate Stay Night", 100));
        items.add(new Project("Persona 4", "Welcome to the NHK", 80));
        items.add(new Project("Persona 5", "Suzumiya Haruhi", 35));

        recycler = (RecyclerView) findViewById(R.id.listView);
        //recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

        // Crear un nuevo adaptador
        adapter = new UserProjectAdapter(items);
        recycler.setAdapter(adapter);
    }

    private void dialogAddUser() {
        List<Project> items = new ArrayList<>();

        items.add(new Project("Persona 1", "Angel Beats", 20));
        items.add(new Project("Persona 2", "Death Note", 30));
        items.add(new Project("Persona 3", "Fate Stay Night", 100));
        items.add(new Project("Persona 4", "Welcome to the NHK", 80));
        items.add(new Project("Persona 5", "Suzumiya Haruhi", 35));

        alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View convertView = inflater.inflate(R.layout.dialog_user_selector, null);
        alertDialog.setView(convertView);




        recycler = (RecyclerView) findViewById(R.id.listView);
        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);
        recycler.setHasFixedSize(true);

        adapter = new UserProjectAdapter(items);
        recycler.setAdapter(adapter);
        alertDialog.setCancelable(true);
        dialog = alertDialog.create();
        dialog = alertDialog.show();
        dialog.setCanceledOnTouchOutside(true);



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

}
