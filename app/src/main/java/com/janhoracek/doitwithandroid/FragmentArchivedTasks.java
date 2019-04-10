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


    private ArchiveTaskViewModel archiveTaskViewModel;
    private RecyclerView mRecyclerView;
    private TaskAdapterArchive mAdapterArchive;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_archived_tasks, container, false);
        mRecyclerView = v.findViewById(R.id.task_fragment_recyclerview);
        archiveTaskViewModel = ViewModelProviders.of(this).get(ArchiveTaskViewModel.class);
        archiveTaskViewModel.getAllTasks().observe(this, new Observer<List<ArchivedTasks>>() {
            @Override
            public void onChanged(@Nullable List<ArchivedTasks> taskers) {
                mAdapterArchive.submitList(taskers);
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

        mAdapterArchive = new TaskAdapterArchive(getActivity());
        mRecyclerView.setAdapter(mAdapterArchive);


        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                archiveTaskViewModel.delete(mAdapterArchive.getTaskAt(viewHolder.getAdapterPosition()));
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(getActivity(), c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary))
                        .addSwipeLeftActionIcon(R.drawable.ic_delete_sweep_black_24dp)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent))
                        .addSwipeRightActionIcon(R.drawable.ic_delete_sweep_black_24dp)
                        .addSwipeRightLabel("Doprava")
                        .setSwipeRightLabelColor(Color.WHITE)
                        .addSwipeLeftLabel("Delete")
                        .setSwipeLeftLabelColor(Color.BLACK)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);



        return v;
    }

}
