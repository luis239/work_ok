package com.immagine.workok.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;

import com.immagine.workok.R;
import com.immagine.workok.adapter.ProjectAdapter;
import com.immagine.workok.adapter.UserProjectAdapter;
import com.immagine.workok.model.Project;
import com.immagine.workok.model.User;

import java.util.ArrayList;
import java.util.List;

public class ProyectDetailsActivity extends AppCompatActivity {


    private Activity act;
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private List<Project> items = new ArrayList<>();
    private FloatingActionButton addTask;
    private FloatingActionButton addUser;
    private FloatingActionButton editProyect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proyect_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
/*        items.add(new User("Luis Fagundez", 2));
        items.add(new User("Jumi", 3));
        items.add(new User("Ricardo", 1));
        items.add(new User("Jose", 8));
        items.add(new User("Kira", 5));
        items.add(new User("Naruto", 10));
        items.add(new User("Danerys", 35));
        items.add(new User("Jon Snow", 80));
        items.add(new User("Rick Grimes", 6));*/

        recycler = (RecyclerView) findViewById(R.id.reciclador);
        //recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(act);
        recycler.setLayoutManager(lManager);

        // Crear un nuevo adaptador
        adapter = new ProjectAdapter(items);
        recycler.setAdapter(adapter);

        addTask = (FloatingActionButton) findViewById(R.id.add_task_fab);
        addUser = (FloatingActionButton) findViewById(R.id.add_user_fab);
        editProyect = (FloatingActionButton) findViewById(R.id.edit_proyect_fab);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(ProyectDetailsActivity.this,NewTaskActivity.class);
                startActivity(intent);
            }
        });
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(ProyectDetailsActivity.this,AddUserProject.class);
                startActivity(intent);
            }
        });
        editProyect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(ProyectDetailsActivity.this,NewProjectActivity.class);
                startActivity(intent);
            }
        });
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
