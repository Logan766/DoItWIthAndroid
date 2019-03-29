package com.janhoracek.doitwithandroid;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.janhoracek.doitwithandroid.Database.Taskers;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class TaskFragment extends Fragment {
    public static final int ADD_TASK_REQUEST = 1;
    public static final int EDIT_TASK_REQUEST = 2;

    private FloatingActionButton mFloatingActionButton;
    private TaskViewModel taskViewModel;
    private RecyclerView mRecyclerView;
    private TaskAdapterAll mAdapterAll;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tasks, container, false);
        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        mRecyclerView = v.findViewById(R.id.task_fragment_recyclerview);
        mFloatingActionButton = v.findViewById(R.id.add_task_fab);

        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(this, new Observer<List<Taskers>>() {
            @Override
            public void onChanged(@Nullable List<Taskers> taskers) {
                //update ReyclerView
                mAdapterAll.setTasks(taskers);
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

        mAdapterAll = new TaskAdapterAll();
        mRecyclerView.setAdapter(mAdapterAll);

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AddEditTaskActivity.class);
                startActivityForResult(i, ADD_TASK_REQUEST);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                taskViewModel.delete(mAdapterAll.getTaskAt(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(mRecyclerView);

        mAdapterAll.setOnTaskClickListener(new TaskAdapterAll.OnTaskClickListener() {
            @Override
            public void onTaskClick(Taskers task) {
                Intent intent = new Intent(getActivity(), AddEditTaskActivity.class);
                intent.putExtra(AddEditTaskActivity.EXTRA_ID, task.getId());
                intent.putExtra(AddEditTaskActivity.EXTRA_TITLE, task.getName());
                intent.putExtra(AddEditTaskActivity.EXTRA_DESCRIPTION, task.getDescription());
                intent.putExtra(AddEditTaskActivity.EXTRA_DEADLINE, new Date(task.getD_time_milisec()));
                intent.putExtra(AddEditTaskActivity.EXTRA_DURATION, task.getTime_consumption());
                intent.putExtra(AddEditTaskActivity.EXTRA_PRIORITY, task.getPriority());
                startActivityForResult(intent, EDIT_TASK_REQUEST);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_TASK_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditTaskActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditTaskActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditTaskActivity.EXTRA_PRIORITY, 1);
            int duration = data.getIntExtra(AddEditTaskActivity.EXTRA_DURATION, 1);
            Date deadline = (Date) data.getSerializableExtra(AddEditTaskActivity.EXTRA_DEADLINE);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(deadline);

            Taskers task = new Taskers(title, description, priority, duration, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) +1, calendar.get(Calendar.YEAR), calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE), deadline.getTime() );
            taskViewModel.insert(task);

            Toast.makeText(getActivity(), "Task added successfully", Toast.LENGTH_SHORT).show();
        } else if(requestCode == EDIT_TASK_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditTaskActivity.EXTRA_ID, -1);

            if(id == -1) {
                Toast.makeText(getActivity(), "Task cannot be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(AddEditTaskActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditTaskActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditTaskActivity.EXTRA_PRIORITY, 1);
            int duration = data.getIntExtra(AddEditTaskActivity.EXTRA_DURATION, 1);
            Date deadline = (Date) data.getSerializableExtra(AddEditTaskActivity.EXTRA_DEADLINE);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(deadline);

            Taskers task = new Taskers(title, description, priority, duration, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) +1, calendar.get(Calendar.YEAR), calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE), deadline.getTime() );
            task.setId(id);
            taskViewModel.update(task);

            Toast.makeText(getActivity(), "Task updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Task not saved", Toast.LENGTH_SHORT).show();
        }
    }


}
