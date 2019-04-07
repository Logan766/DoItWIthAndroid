package com.janhoracek.doitwithandroid;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.janhoracek.doitwithandroid.Database.Taskers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import static android.graphics.Color.rgb;

public class TaskAdapterAll extends ListAdapter<Taskers, TaskAdapterAll.TaskHolder> {
    private OnTaskClickListener mListener;
    private SimpleDateFormat mDateFormat;

    public TaskAdapterAll() {
        super(DIFF_CALLBACK);
    }

    public static final DiffUtil.ItemCallback<Taskers> DIFF_CALLBACK = new DiffUtil.ItemCallback<Taskers>() {
        @Override
        public boolean areItemsTheSame(@NonNull Taskers oldItem, @NonNull Taskers newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Taskers oldItem, @NonNull Taskers newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getPriority() == newItem.getPriority() &&
                    oldItem.getTime_consumption() == newItem.getTime_consumption() &&
                    oldItem.getD_time_milisec() == newItem.getD_time_milisec() &&
                    oldItem.getExp() == newItem.getExp() &&
                    oldItem.getD_day() == newItem.getD_day() &&
                    oldItem.getD_month() == newItem.getD_month() &&
                    oldItem.getD_year() == newItem.getD_year() &&
                    oldItem.getId() == newItem.getId() &&
                    oldItem.getD_time().equals(newItem.getD_time());
        }
    };

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item_all, parent, false);
        return new TaskHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        mDateFormat = new SimpleDateFormat("d.M.yyyy    HH:mm");
        Taskers currentTaskers = getItem(position);
        switch (currentTaskers.getPriority()) {
            case 1:
                holder.mPriority.setBackgroundColor(rgb(239, 83, 80));
                break;
            case 2:
                holder.mPriority.setBackgroundColor(rgb(255,202,40));
                break;
            case 3:
                holder.mPriority.setBackgroundColor(rgb(156,204,101));
                break;
        }
        holder.mTextViewTitle.setText(currentTaskers.getName());
        holder.mTextViewDescription.setText(currentTaskers.getDescription());
        holder.mTextViewEstTime.setText(String.valueOf(currentTaskers.getTime_consumption()));
        holder.mTextViewDeadline.setText(mDateFormat.format(currentTaskers.getD_time_milisec()));
    }


    public Taskers getTaskAt(int position) {
        return getItem(position);
    }

    class TaskHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewTitle;
        private TextView mTextViewDescription;
        private RelativeLayout mRelativeLayout;
        private TextView mTextViewDeadline;
        private TextView mTextViewEstTime;
        private View mPriority;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            mRelativeLayout = itemView.findViewById(R.id.task_backgroud);
            mTextViewTitle = itemView.findViewById(R.id.text_view_title);
            mTextViewDescription = itemView.findViewById(R.id.text_view_description);
            mTextViewDeadline = itemView.findViewById(R.id.text_view_deadline_data);
            mTextViewEstTime = itemView.findViewById(R.id.text_view_time_consumption_data);
            mPriority = itemView.findViewById(R.id.priority);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (mListener != null && position != RecyclerView.NO_POSITION) {
                        mListener.onTaskClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnTaskClickListener {
        void onTaskClick(Taskers task);
    }

    public void setOnTaskClickListener(OnTaskClickListener listener) {
        this.mListener = listener;
    }
}
