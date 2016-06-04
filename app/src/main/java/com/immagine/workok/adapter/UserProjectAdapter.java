package com.immagine.workok.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.immagine.workok.R;
import com.immagine.workok.model.Project;

import java.util.List;

/**
 * Created by Alejandro on 29/05/2016.
 */
public class UserProjectAdapter extends RecyclerView.Adapter<UserProjectAdapter.UserProjectViewHolder> {


    private List<Project> items;


    public static class UserProjectViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public ImageView imagen;
        public TextView nombre;

        public UserProjectViewHolder(View v) {
            super(v);
            //imagen = (ImageView) v.findViewById(R.id.imagen);
            nombre = (TextView) v.findViewById(R.id.nombre);
        }
    }

    public UserProjectAdapter(List<Project> items) {

        this.items = items;
    }

    @Override
    public UserProjectViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_card, viewGroup, false);
        return new UserProjectViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserProjectViewHolder holder, int position) {

        //holder.imagen.setImageResource(items.get(i).getImagen());
        holder.nombre.setText(items.get(position).getTitle());
        //holder.visitas.setText("Completado: "+String.valueOf(items.get(position).getPercentage())+"%");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}







