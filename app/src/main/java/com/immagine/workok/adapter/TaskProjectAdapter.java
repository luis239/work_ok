package com.immagine.workok.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.immagine.workok.R;
import com.immagine.workok.model.Task;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Alejandro on 29/05/2016.
 */
public class TaskProjectAdapter extends RecyclerView.Adapter<TaskProjectAdapter.TaskViewHolder> {


    private List<Task> items;
    private boolean isDetail = false;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onClick(View v, int index);
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {
        private final TextView projectName;
        // Campos respectivos de un item
        public ImageView imageButton;
        public TextView name;
        public TextView userAssigned;
        public TextView percent;
        public ImageView delete;
        public RelativeLayout layout;
        private View line;

        public TaskViewHolder(View v) {
            super(v);
            delete = (ImageView) v.findViewById(R.id.delete);
            imageButton = (ImageView) v.findViewById(R.id.imageButton);
            name = (TextView) v.findViewById(R.id.nombre);
            userAssigned = (TextView) v.findViewById(R.id.user_assigend);
            percent = (TextView) v.findViewById(R.id.percent);
            imageButton.setImageResource(android.R.drawable.ic_menu_edit);
            projectName = (TextView) v.findViewById(R.id.projectName);
            layout = (RelativeLayout) v.findViewById(R.id.card_view);
            layout.setBackgroundResource(R.color.light_blue);
            line = v.findViewById(R.id.linea);
            line.setBackgroundResource(R.color.line_color);
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

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.project_card, viewGroup, false);
        return new TaskViewHolder(v);
    }



    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {

        holder.name.setText(items.get(position).getTitle());
        holder.percent.setText("Completado: "+items.get(position).getPercentage()+"%");
        holder.projectName.setText("Proyecto: "+items.get(position).getProjectName());
        if(items.get(position).getFullname() != null)
            holder.userAssigned.setText("Asignado a: " + items.get(position).getFullname());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}







