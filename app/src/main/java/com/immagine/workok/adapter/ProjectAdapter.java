package com.immagine.workok.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.immagine.workok.R;
import com.immagine.workok.model.Project;

import java.util.List;

/**
 * Created by Alejandro on 29/05/2016.
 */
public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {


    private List<Project> items;
    private boolean isDetail = false;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onClick(View v, int index);
    }

    public class ProjectViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {
        private final ImageView delete;
        public TextView percent;
        public ImageView imagen;
        public TextView nombre;
        public TextView userAssigned;
        public TextView projectName;
        public RoundCornerProgressBar progressBar;
        public RelativeLayout layout;
        public Context context;
        public TextView assignedBy;
        public ProjectViewHolder(View v) {
            super(v);
            context = v.getContext();
            imagen = (ImageView) v.findViewById(R.id.imageButton);
            delete = (ImageView) v.findViewById(R.id.delete);
            percent = (TextView) v.findViewById(R.id.percent);
            nombre = (TextView) v.findViewById(R.id.nombre);
            userAssigned = (TextView) v.findViewById(R.id.user_assigend);
            assignedBy = (TextView) v.findViewById(R.id.projectName);
            layout = (RelativeLayout) v.findViewById(R.id.card_view);
            projectName = (TextView) v.findViewById(R.id.assigned_by);

            progressBar = (RoundCornerProgressBar) v.findViewById(R.id.progressBar);
            progressBar.setMax(100);
            layout.setOnClickListener(this);
            imagen.setOnClickListener(this);
            delete.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            listener.onClick(v,getAdapterPosition());
        }
    }

    public ProjectAdapter(List<Project> items) {

        this.items = items;
    }

        public ProjectAdapter(List<Project> items,OnItemClickListener listener) {

            this.items = items;
            this.listener = listener;
        }

    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.project_card, viewGroup, false);
        return new ProjectViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProjectViewHolder holder, int position) {
        int percentage = items.get(position).getPercentage();
        holder.nombre.setText(items.get(position).getTitle());
        //holder.percent.setVisibility(View.GONE);
        holder.projectName.setVisibility(View.GONE);
        holder.delete.setVisibility(View.GONE);
        holder.assignedBy.setVisibility(View.INVISIBLE);
        holder.userAssigned.setVisibility(View.INVISIBLE);
        if (isDetail){
            holder.percent.setText("Completado: "+String.valueOf(items.get(position).getPercentage())+"%");
        }else {
            holder.percent.setText(String.valueOf(percentage) + "%");
            holder.progressBar.setProgress(percentage);
            if(percentage < 50) {
                holder.progressBar.setProgressColor(ContextCompat.getColor(holder.context, R.color.red));
            }
            if((percentage >= 50)&&(percentage < 100)) {
                holder.progressBar.setProgressColor(ContextCompat.getColor(holder.context, R.color.yellow));
                holder.percent.setTextColor(Color.BLACK);
            }
            if(percentage == 100) {
                holder.progressBar.setProgressColor(ContextCompat.getColor(holder.context, R.color.green));
                holder.percent.setTextColor(Color.BLACK);
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}







