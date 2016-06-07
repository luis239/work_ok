package com.immagine.workok.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.immagine.workok.R;
import com.immagine.workok.model.Project;
import com.immagine.workok.model.User;

import java.util.List;

/**
 * Created by Alejandro on 29/05/2016.
 */
public class UserProjectAdapter extends RecyclerView.Adapter<UserProjectAdapter.UserProjectViewHolder>{


    private List<User> items;
    private boolean isAdd = false;


    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onClick(UserProjectViewHolder holder, int idPromocion);
    }

    public class UserProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Campos respectivos de un item
        public ImageView imagen;
        public TextView nombre;
        public CheckBox checkBox;

        public UserProjectViewHolder(View v) {
            super(v);
            imagen = (ImageView) v.findViewById(R.id.imageButton);
            nombre = (TextView) v.findViewById(R.id.nombre);
            checkBox = (CheckBox) v.findViewById(R.id.checkBox);
            imagen.setOnClickListener(this);
            checkBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(this,getAdapterPosition());
        }
    }

    public UserProjectAdapter(List<User> items) {

        this.items = items;
    }

    public UserProjectAdapter(List<User> items, OnItemClickListener listener) {

        this.listener = listener;
        this.items = items;
    }


    public UserProjectAdapter(List<User> items, boolean isAdd, OnItemClickListener listener) {

        this.items = items;
        this.isAdd = isAdd;
        this.listener = listener;
    }

    @Override
    public UserProjectViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_card, viewGroup, false);
        return new UserProjectViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final UserProjectViewHolder holder, final int position) {

        if(isAdd) {
            holder.imagen.setVisibility(View.INVISIBLE);
            holder.checkBox.setOnCheckedChangeListener(null);
            if(items.get(position).isSelected()){

                holder.checkBox.setChecked(true);
            }else{

                holder.checkBox.setChecked(false);
            }


        }else{

            holder.checkBox.setVisibility(View.INVISIBLE);
        }

        holder.nombre.setText(items.get(position).getUsername());

    }


    @Override
    public int getItemCount() {
        return items.size();
    }
}







