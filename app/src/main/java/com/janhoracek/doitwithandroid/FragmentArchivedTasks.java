package com.janhoracek.doitwithandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.janhoracek.doitwithandroid.Database.ArchiveTaskViewModel;
import com.janhoracek.doitwithandroid.Database.ArchivedTasks;
import com.janhoracek.doitwithandroid.Database.TaskViewModel;
import com.janhoracek.doitwithandroid.Database.Taskers;
import com.takusemba.spotlight.OnSpotlightStateChangedListener;
import com.takusemba.spotlight.OnTargetStateChangedListener;
import com.takusemba.spotlight.Spotlight;
import com.takusemba.spotlight.shape.RoundedRectangle;
import com.takusemba.spotlight.target.SimpleTarget;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import static android.app.Activity.RESULT_OK;

public class FragmentArchivedTasks extends Fragment {
    public static final int REOPEN_TASK_REQUEST = 3;

    private ArchiveTaskViewModel mArchiveTaskViewModel;
    private RecyclerView mRecyclerView;
    private TaskAdapterArchive mAdapterArchive;
    private TaskViewModel mTaskViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_archived_tasks, container, false);
        mRecyclerView = v.findViewById(R.id.task_fragment_recyclerview);

        mTaskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        mArchiveTaskViewModel = ViewModelProviders.of(this).get(ArchiveTaskViewModel.class);
        mArchiveTaskViewModel.getAllTasks().observe(this, new Observer<List<ArchivedTasks>>() {
            @Override
            public void onChanged(@Nullable List<ArchivedTasks> taskers) {
                mAdapterArchive.submitList(taskers);
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

        mAdapterArchive = new TaskAdapterArchive(getActivity());
        mRecyclerView.setAdapter(mAdapterArchive);



        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mArchiveTaskViewModel.delete(mAdapterArchive.getTaskAt(viewHolder.getAdapterPosition()));
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(getActivity(), c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary))
                        .addSwipeLeftActionIcon(R.drawable.ic_delete_black_24dp)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent))
                        .addSwipeRightActionIcon(R.drawable.ic_delete_sweep_black_24dp)
                        .addSwipeRightLabel("Doprava")
                        .setSwipeRightLabelColor(Color.WHITE)
                        .addSwipeLeftLabel(getString(R.string.task_delete))
                        .setSwipeLeftLabelColor(Color.BLACK)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        mAdapterArchive.setOnTaskClickListener(new TaskAdapterArchive.OnTaskClickListener() {
            @Override
            public void onTaskClick(ArchivedTasks task) {
                Intent intent = new Intent(getActivity(), AddEditTaskActivity.class);
                intent.putExtra(AddEditTaskActivity.REOPEN_REQUEST, 3);
                intent.putExtra(AddEditTaskActivity.EXTRA_ID, task.getId());
                intent.putExtra(AddEditTaskActivity.EXTRA_TITLE, task.getName());
                intent.putExtra(AddEditTaskActivity.EXTRA_DESCRIPTION, task.getDescription());
                intent.putExtra(AddEditTaskActivity.EXTRA_DEADLINE, new Date(task.getD_time_milisec()));
                intent.putExtra(AddEditTaskActivity.EXTRA_DURATION, task.getTime_consumption());
                intent.putExtra(AddEditTaskActivity.EXTRA_PRIORITY, task.getPriority());
                startActivityForResult(intent, REOPEN_TASK_REQUEST);

            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REOPEN_TASK_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditTaskActivity.EXTRA_ID, -1);

            if(id == -1) {
                Toast.makeText(getActivity(), getString(R.string.fragment_archived_tasks_cannot_update_task), Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(AddEditTaskActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditTaskActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditTaskActivity.EXTRA_PRIORITY, 1);
            int duration = data.getIntExtra(AddEditTaskActivity.EXTRA_DURATION, 1);
            Date deadline = (Date) data.getSerializableExtra(AddEditTaskActivity.EXTRA_DEADLINE);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(deadline);

            Taskers task = new Taskers(title, description, priority, duration, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) +1, calendar.get(Calendar.YEAR), calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE), deadline.getTime(), 0, 0 );
            mTaskViewModel.insert(task);

            Toast.makeText(getActivity(), getString(R.string.fragment_archived_tasks_reopen_task), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), getString(R.string.fragment_archived_tasks_task_not_reopen), Toast.LENGTH_SHORT).show();
        }
    }

}
