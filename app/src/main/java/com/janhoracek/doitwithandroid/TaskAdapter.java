package com.janhoracek.doitwithandroid;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.janhoracek.doitwithandroid.Database.Taskers;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {

    private List<Taskers> mTasks = new ArrayList<>();

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new TaskHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Taskers currentTaskers = mTasks.get(position);
        switch (currentTaskers.getPriority()) {
            case 1:
                holder.mRelativeLayout.setBackgroundColor(Color.GREEN);
                break;
            case 2:
                holder.mRelativeLayout.setBackgroundColor(Color.YELLOW);
                break;
            case 3:
                holder.mRelativeLayout.setBackgroundColor(Color.RED);
                break;
        }
        holder.mTextViewTitle.setText(currentTaskers.getName());
        holder.mTextViewDescription.setText(currentTaskers.getDescription());
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    public Taskers getTaskAt(int position) {
        return mTasks.get(position);
    }

    public void setTasks (List<Taskers> tasks) {
        this.mTasks = tasks;
        notifyDataSetChanged(); //replace

    }


    class TaskHolder extends  RecyclerView.ViewHolder {
        private TextView mTextViewTitle;
        private TextView mTextViewDescription;
        private RelativeLayout mRelativeLayout;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            mRelativeLayout = itemView.findViewById(R.id.task_backgroud);
            mTextViewTitle = itemView.findViewById(R.id.text_view_title);
            mTextViewDescription = itemView.findViewById(R.id.text_view_description);
        }
    }
}
