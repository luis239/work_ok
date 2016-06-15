package com.immagine.workok.adapter;

import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.immagine.workok.R;
import com.immagine.workok.model.Notifications;
import com.immagine.workok.model.Project;

import java.util.List;

/**
 * Created by Alejandro on 29/05/2016.
 */
public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ListViewHolder> {


    private List<Notifications> items;
    private boolean isDetail = false;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onClick(View v, int index);
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {


        public TextView title;
        public TextView message;
        public ListViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            message = (TextView) v.findViewById(R.id.message);
        }
    }

    public NotificationsAdapter(List<Notifications> items) {

        this.items = items;
    }

    /*public NotificationsAdapter(List<Project> items, OnItemClickListener listener) {

        this.items = items;
        this.listener = listener;
    }*/


    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row, viewGroup, false);
        return new ListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {

        holder.title.setText(items.get(position).getTitle());
        holder.message.setText(items.get(position).getMessage());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}







