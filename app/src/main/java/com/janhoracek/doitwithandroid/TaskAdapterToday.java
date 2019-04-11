package com.janhoracek.doitwithandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.janhoracek.doitwithandroid.Database.Taskers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import static android.graphics.Color.rgb;

public class TaskAdapterToday extends RecyclerView.Adapter<TaskAdapterToday.TaskHolder> {
    private static final String PREFS_NAME = "com.janhoracek.doitwithandroid.SettingsSharedPrefs";
    private static final String START_HOUR = "com.janhoracek.doitwithandroid.START_HOUR";
    private static final String START_MINUTE = "com.janhoracek.doitwithandroid.START_MINUTE";
    private static final String END_HOUR = "com.janhoracek.doitwithandroid.END_HOUR";
    private static final String END_MINUTE = "com.janhoracek.doitwithandroid.END_MINUTE";

    private static final String TAG = "MRDEJ";


    private List<Taskers> mTasks = new ArrayList<>();

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item_today, parent, false);
        return new TaskHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Context context = ApplicationActivity.contextOfApplication;
        SharedPreferences pref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Calendar calStart = Calendar.getInstance();
        calStart.set(Calendar.HOUR_OF_DAY, pref.getInt(START_HOUR, -1));
        calStart.set(Calendar.MINUTE, pref.getInt(START_MINUTE, -1));
        calStart.set(Calendar.SECOND, 0);

        Calendar calEnd = Calendar.getInstance();
        calEnd.set(Calendar.HOUR_OF_DAY, pref.getInt(END_HOUR, -1));
        calEnd.set(Calendar.MINUTE, pref.getInt(END_MINUTE, -1));
        calEnd.set(Calendar.SECOND, 0);

        Taskers currentTaskers = mTasks.get(position);
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

        if(!((calStart.getTimeInMillis() < new DateHandler().getCurrentDateTimeInMilisec()) && (new DateHandler().getCurrentDateTimeInMilisec() < calEnd.getTimeInMillis()))) {
            holder.mBackground.setBackgroundColor(rgb(200, 200, 200));
        }

        holder.mTextViewTitle.setText(currentTaskers.getName());
        holder.mTextViewDescription.setText(currentTaskers.getDescription());


        Log.d(TAG, "To be done: " + currentTaskers.getTo_be_done());
        if(currentTaskers.getTo_be_done() > 0) {
            holder.mTextViewCompleted.setVisibility(View.VISIBLE);
            int partDone = Math.round((currentTaskers.getTo_be_done() / (float) currentTaskers.getTime_consumption()) * 100);
            holder.mTextViewCompleted.setText(partDone + "%");
            holder.mTextViewExp.setText(String.valueOf(Math.round((currentTaskers.getTo_be_done() / (float) currentTaskers.getTime_consumption()) * currentTaskers.getExp())) + " XP");
        } else {
            holder.mTextViewCompleted.setVisibility(View.INVISIBLE);
            holder.mTextViewExp.setText(String.valueOf(currentTaskers.getExp()) + " XP");
        }

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
        private CardView mBackground;
        private TextView mTextViewExp;
        private View mPriority;
        private TextView mTextViewCompleted;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            mBackground = itemView.findViewById(R.id.today_background);
            mTextViewTitle = itemView.findViewById(R.id.text_view_title);
            mTextViewDescription = itemView.findViewById(R.id.text_view_description);
            mTextViewExp = itemView.findViewById(R.id.exp_task);
            mPriority = itemView.findViewById(R.id.priority_today);
            mTextViewCompleted = itemView.findViewById(R.id.completed_percent);
        }
    }
}
