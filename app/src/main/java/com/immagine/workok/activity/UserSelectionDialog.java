package com.immagine.workok.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.immagine.workok.R;
import com.immagine.workok.adapter.UserProjectAdapter;
import com.immagine.workok.model.Project;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alejandro on 04/06/2016.
 */
public class UserSelectionDialog extends Dialog {


    private Activity act;
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private Button add;
    private ImageView imageButton;

    public UserSelectionDialog(Activity a) {
        super(a);
        this.act = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_user_selector);

        this.setTitle(R.string.select_add_user);
        List<Project> items = new ArrayList<>();

        items.add(new Project("Persona 1", "Angel Beats", 20));
        items.add(new Project("Persona 2", "Death Note", 30));
        items.add(new Project("Persona 3", "Fate Stay Night", 100));
        items.add(new Project("Persona 4", "Welcome to the NHK", 80));
        items.add(new Project("Persona 5", "Suzumiya Haruhi", 35));
        items.add(new Project("Persona 4", "Welcome to the NHK", 80));
        items.add(new Project("Persona 5", "Suzumiya Haruhi", 35));
        items.add(new Project("Persona 4", "Welcome to the NHK", 80));
        items.add(new Project("Persona 5", "Suzumiya Haruhi", 35));

        recycler = (RecyclerView) findViewById(R.id.recycler_view);
        //recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(act);
        recycler.setLayoutManager(lManager);

        // Crear un nuevo adaptador
        adapter = new UserProjectAdapter(items);
        recycler.setAdapter(adapter);
    }
}
