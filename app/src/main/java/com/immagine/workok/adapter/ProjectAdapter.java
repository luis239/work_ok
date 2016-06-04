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
public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {


    private List<Project> items;


    public static class ProjectViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public ImageView imagen;
        public TextView nombre;
        public TextView visitas;

        public ProjectViewHolder(View v) {
            super(v);
            //imagen = (ImageView) v.findViewById(R.id.imagen);
            nombre = (TextView) v.findViewById(R.id.nombre);
            visitas = (TextView) v.findViewById(R.id.visitas);
        }
    }

    public ProjectAdapter(List<Project> items) {

        this.items = items;
    }

    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.project_card, viewGroup, false);
        return new ProjectViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProjectViewHolder holder, int position) {

        //holder.imagen.setImageResource(items.get(i).getImagen());
        holder.nombre.setText(items.get(position).getTitle());
        holder.visitas.setText("Completado: "+String.valueOf(items.get(position).getPercentage())+"%");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}







