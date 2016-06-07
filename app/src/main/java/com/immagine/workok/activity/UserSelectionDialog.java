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
public class UserSelectionDialog extends Dialog implements UserProjectAdapter.OnItemClickListener,View.OnClickListener{


    private Activity act;
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private Button add;
    private ImageView imageButton;
    private List<User> items = new ArrayList<>();
    private OnClickAddUser listener;
    private List<User>userSelected = new ArrayList<>();

    public UserSelectionDialog(Activity a,OnClickAddUser listener) {
        super(a);
        this.act = a;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_user_selector);

        this.setTitle(R.string.select_add_user);


        items.add(new User("Luis Fagundez", 2));
        items.add(new User("Jumi", 3));
        items.add(new User("Ricardo", 1));
        items.add(new User("Jose", 8));
        items.add(new User("Kira", 5));
        items.add(new User("Naruto", 10));
        items.add(new User("Danerys", 35));
        items.add(new User("Jon Snow", 80));
        items.add(new User("Rick Grimes", 6));

        recycler = (RecyclerView) findViewById(R.id.recycler_view);
        add = (Button) findViewById(R.id.button2);
        add.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case (R.id.button2):{

                for (User u:items) {
                    if (u.isSelected())
                        userSelected.add(u);
                }
            }
        }
        listener.onClickAdd(userSelected);
        this.cancel();
    }

    public interface OnClickAddUser{

        void onClickAdd(List<User> items);
    }
}
