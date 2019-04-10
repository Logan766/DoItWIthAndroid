package com.janhoracek.doitwithandroid;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.janhoracek.doitwithandroid.Database.ArchivedTasks;
import com.janhoracek.doitwithandroid.Database.Taskers;

import java.text.SimpleDateFormat;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import static android.graphics.Color.rgb;

public class TaskAdapterArchive extends ListAdapter<ArchivedTasks, TaskAdapterArchive.TaskHolder> {
    private OnTaskClickListener mListener;
    private SimpleDateFormat mDateFormat;
    private Context mContext;

    public TaskAdapterArchive(Context context) {
        super(DIFF_CALLBACK);
        mContext = context;
    }

    public static final DiffUtil.ItemCallback<ArchivedTasks> DIFF_CALLBACK = new DiffUtil.ItemCallback<ArchivedTasks>() {
        @Override
        public boolean areItemsTheSame(@NonNull ArchivedTasks oldItem, @NonNull ArchivedTasks newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ArchivedTasks oldItem, @NonNull ArchivedTasks newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getPriority() == newItem.getPriority() &&
                    oldItem.getTime_consumption() == newItem.getTime_consumption() &&
                    oldItem.getD_time_milisec() == newItem.getD_time_milisec() &&
                    oldItem.getId() == newItem.getId();
        }
    };

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item_archive, parent, false);
        return new TaskHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        mDateFormat = new SimpleDateFormat("d.M.yyyy    HH:mm");
        ArchivedTasks currentTaskers = getItem(position);
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


        if(currentTaskers.getD_time_milisec() < new DateHandler().getCurrentDateTimeInMilisec()) {
            Log.d("FIREEE", "FIREEEEEE");
            holder.mTextViewFire.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_whatshot_red_24dp), null, null, null);
        } else {
            holder.mTextViewFire.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_whatshot_black_24dp), null, null, null);
        }
        holder.mTextViewTitle.setText(currentTaskers.getName());
        holder.mTextViewDescription.setText(currentTaskers.getDescription());
        holder.mTextViewEstTime.setText(String.valueOf(currentTaskers.getTime_consumption()));
        holder.mTextViewDeadline.setText(mDateFormat.format(currentTaskers.getD_time_milisec()));
        holder.mTextViewCompleted.setText(mDateFormat.format(currentTaskers.getCompleted()));
    }


    public ArchivedTasks getTaskAt(int position) {
        return getItem(position);
    }

    class TaskHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewTitle;
        private TextView mTextViewDescription;
        private RelativeLayout mRelativeLayout;
        private TextView mTextViewDeadline;
        private TextView mTextViewEstTime;
        private View mPriority;
        private TextView mTextViewFire;
        private TextView mTextViewCompleted;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            mRelativeLayout = itemView.findViewById(R.id.task_backgroud);
            mTextViewTitle = itemView.findViewById(R.id.text_view_title);
            mTextViewDescription = itemView.findViewById(R.id.text_view_description);
            mTextViewDeadline = itemView.findViewById(R.id.text_view_deadline_data);
            mTextViewEstTime = itemView.findViewById(R.id.text_view_time_consumption_data);
            mPriority = itemView.findViewById(R.id.priority);
            mTextViewFire = itemView.findViewById(R.id.text_view_deadline);
            mTextViewCompleted = itemView.findViewById(R.id.text_view_time_completed_data);


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
        void onTaskClick(ArchivedTasks task);
    }

    public void setOnTaskClickListener(OnTaskClickListener listener) {
        this.mListener = listener;
    }
}
