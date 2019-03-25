package com.janhoracek.doitwithandroid;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static android.app.Activity.RESULT_OK;

public class TaskFragment extends Fragment {
    public static final int ADD_NOTE_REQUEST = 1;

    FloatingActionButton mFloatingActionButton;
    private TaskViewModel taskViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tasks, container, false);
        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        mFloatingActionButton = v.findViewById(R.id.add_task_fab);

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AddTaskActivity.class);
                startActivityForResult(i, ADD_NOTE_REQUEST);
            }
        });


        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddTaskActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddTaskActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddTaskActivity.EXTRA_PRIORITY, 1);

            Taskers task = new Taskers(title, description);
            taskViewModel.insert(task);

            Toast.makeText(getActivity(), "Task added successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Task not saved", Toast.LENGTH_SHORT).show();
        }
    }
}
