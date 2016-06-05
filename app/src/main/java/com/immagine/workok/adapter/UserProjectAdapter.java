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
public class UserProjectAdapter extends RecyclerView.Adapter<UserProjectAdapter.UserProjectViewHolder>{


    private List<Project> items;
    private boolean isAdd = false;


    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onClick(UserProjectViewHolder holder, int idPromocion);
    }

    public class UserProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Campos respectivos de un item
        public ImageView imagen;
        public TextView nombre;

        public UserProjectViewHolder(View v) {
            super(v);
            imagen = (ImageView) v.findViewById(R.id.imageButton);
            nombre = (TextView) v.findViewById(R.id.nombre);
            imagen.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(this,getAdapterPosition());
        }
    }

    public UserProjectAdapter(List<Project> items) {

        this.items = items;
    }

    public UserProjectAdapter(List<Project> items, OnItemClickListener listener) {

        this.listener = listener;
        this.items = items;
    }


    public UserProjectAdapter(List<Project> items, boolean isAdd) {

        this.items = items;
        this.isAdd = isAdd;
    }

    @Override
    public UserProjectViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_card, viewGroup, false);
        return new UserProjectViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserProjectViewHolder holder, int position) {

        if(isAdd)
            holder.imagen.setImageResource(R.drawable.ic_checkmark_48);

        holder.nombre.setText(items.get(position).getTitle());
        //holder.visitas.setText("Completado: "+String.valueOf(items.get(position).getPercentage())+"%");
    }



    @Override
    public int getItemCount() {
        return items.size();
    }
}







