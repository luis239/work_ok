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
import com.immagine.workok.model.Task;

import java.util.List;

/**
 * Created by Alejandro on 29/05/2016.
 */
public class TaskProjectAdapter extends RecyclerView.Adapter<TaskProjectAdapter.TaskViewHolder> {


    private int statusID = 0;
    private List<Task> items;
    private boolean isDetail = false;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onClick(View v, int index);
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {
        private final TextView projectName;
        private Context context;
        // Campos respectivos de un item
        public ImageView imageButton;
        public TextView name;
        public TextView userAssigned;
        public TextView percent;
        public TextView assignedBy;
        public ImageView delete;
        public RelativeLayout layout;
        private View line;
        public RoundCornerProgressBar progressBar;

        public TaskViewHolder(View v) {
            super(v);
            context = v.getContext();
            delete = (ImageView) v.findViewById(R.id.delete);
            imageButton = (ImageView) v.findViewById(R.id.imageButton);
            name = (TextView) v.findViewById(R.id.nombre);
            userAssigned = (TextView) v.findViewById(R.id.user_assigend);
            assignedBy = (TextView) v.findViewById(R.id.projectName);
            percent = (TextView) v.findViewById(R.id.percent);
            imageButton.setImageResource(android.R.drawable.ic_menu_edit);
            projectName = (TextView) v.findViewById(R.id.assigned_by);
            layout = (RelativeLayout) v.findViewById(R.id.card_view);
            layout.setBackgroundResource(R.color.light_blue);
            line = v.findViewById(R.id.linea);
            line.setBackgroundResource(R.color.line_color);
            progressBar = (RoundCornerProgressBar) v.findViewById(R.id.progressBar);
            //progressBar.setProgressBackgroundColor(ContextCompat.getColor(context, R.color.blue_grey));
            progressBar.setMax(100);
            layout.setOnClickListener(this);
            delete.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            listener.onClick(v,getAdapterPosition());
        }
    }

    public TaskProjectAdapter(List<Task> items) {

        this.items = items;
    }

    public TaskProjectAdapter(List<Task> items, OnItemClickListener listener) {

        this.items = items;
        this.listener = listener;
    }
    public TaskProjectAdapter(List<Task> items, OnItemClickListener listener,int statusID) {

        this.items = items;
        this.listener = listener;
        this.statusID = statusID;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.project_card, viewGroup, false);
        return new TaskViewHolder(v);
    }



    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        int percentage = items.get(position).getPercentage();
        holder.name.setText(items.get(position).getTitle());
        holder.percent.setText(percentage+"%");
        holder.projectName.setText("Proyecto: "+items.get(position).getProjectName());
        holder.progressBar.setProgress(percentage);

        if(statusID == 7){
            //holder.layout.setClickable(false);
            //holder.layout.setFocusableInTouchMode(false);
            holder.delete.setVisibility(View.INVISIBLE);
            holder.imageButton.setVisibility(View.INVISIBLE);
        }
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
        if(items.get(position).getFullname() != null)
            holder.userAssigned.setText("Asignado a: " + items.get(position).getFullname());

        holder.assignedBy.setText("Asignado por: " + items.get(position).getAssignedBy());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}







