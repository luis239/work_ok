package com.immagine.workok.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerViewAccessibilityDelegate;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.immagine.workok.R;
import com.immagine.workok.adapter.ProjectAdapter;
import com.immagine.workok.adapter.UserProjectAdapter;
import com.immagine.workok.model.Project;

import java.util.ArrayList;
import java.util.List;

import static com.immagine.workok.R.id.reciclador;

public class AddUserProject extends AppCompatActivity implements UserProjectAdapter.OnItemClickListener {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private Button addButton;
    private AlertDialog.Builder alertDialog;
    private AlertDialog dialog;
    private List<Project> items = new ArrayList<>();

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
        adapter = new UserProjectAdapter(items,this);
        recycler.setAdapter(adapter);
    }

    private void dialogAddUser() {
        UserSelectionDialog cd = new UserSelectionDialog(this);
        cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cd.show();

    }


    public void removeItem(View v){

        int selectedItemPos = recycler.getChildAdapterPosition(v);
        RecyclerView.ViewHolder viewHolder = recycler.findViewHolderForAdapterPosition(selectedItemPos);
        /*for (int i = 0; i < items.size(); i++){



        }*/

        items.remove(selectedItemPos);
        adapter.notifyItemRemoved(selectedItemPos);
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
    public void onClick(UserProjectAdapter.UserProjectViewHolder holder, final int idPromocion) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
// Add the buttons
        builder.setMessage("Desea eliminar este usuario?");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                items.remove(idPromocion);
                adapter.notifyItemRemoved(idPromocion);
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
}
