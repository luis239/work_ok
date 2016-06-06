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
import com.immagine.workok.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alejandro on 04/06/2016.
 */
public class UserSelectionDialog extends Dialog implements UserProjectAdapter.OnItemClickListener{


    private Activity act;
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private Button add;
    private ImageView imageButton;
    private List<User> items = new ArrayList<>();
    public UserSelectionDialog(Activity a) {
        super(a);
        this.act = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_user_selector);

        this.setTitle(R.string.select_add_user);


        items.add(new User("Persona 1", 2));
        items.add(new User("Persona 2", 3));
        items.add(new User("Persona 3", 1));
        items.add(new User("Persona 4", 8));
        items.add(new User("Persona 5", 5));
        items.add(new User("Persona 6", 10));
        items.add(new User("Persona 7", 35));
        items.add(new User("Persona 8", 80));
        items.add(new User("Persona 9", 6));

        recycler = (RecyclerView) findViewById(R.id.recycler_view);
        //recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(act);
        recycler.setLayoutManager(lManager);

        // Crear un nuevo adaptador
        adapter = new UserProjectAdapter(items,true,this);
        recycler.setAdapter(adapter);
    }

    @Override
    public void onClick(UserProjectAdapter.UserProjectViewHolder holder, int idPromocion) {

        items.get(idPromocion).setSelected(true);

    }
}
