package com.janhoracek.doitwithandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import devlight.io.library.ArcProgressStackView;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.github.mikephil.charting.charts.Chart;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.janhoracek.doitwithandroid.Database.ArchiveTaskViewModel;
import com.janhoracek.doitwithandroid.Database.ArchivedTasks;
import com.janhoracek.doitwithandroid.Database.StatsViewModel;
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

import static android.app.Activity.RESULT_OK;

public class FragmentCurrentTasks extends Fragment {
    public static final int ADD_TASK_REQUEST = 1;
    public static final int EDIT_TASK_REQUEST = 2;
    private static final String PREFS_NAME = "com.janhoracek.doitwithandroid.SettingsSharedPrefs";
    private static final String HOME_FRAG_RUN = "com.janhoracek.doitwithandroid.HOME_FRAG_RUN";
    private static final String USER_LEVEL = "com.janhoracek.doitwithandroid.USER_LEVEL";
    private static final String USER_EXPERIENCE = "com.janhoracek.doitwithandroid.USER_EXPERIENCE";
    private static final String NEXT_EXPERIENCE = "com.janhoracek.doitwithandroid.NEXT_EXPERIENCE";

    private FloatingActionButton mFloatingActionButton;
    private TaskViewModel taskViewModel;
    private RecyclerView mRecyclerView;
    private TaskAdapterAll mAdapterAll;
    private StatsViewModel mStatsViewModel;
    private ArchiveTaskViewModel mArchiveTaskViewModel;
    private boolean FirstRunCheck;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_tasks_current, container, false);
        final SharedPreferences pref = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        DateChangeChecker.getInstance().CheckDate(pref);

        mRecyclerView = v.findViewById(R.id.task_fragment_recyclerview);
        mFloatingActionButton = v.findViewById(R.id.add_task_fab);

        mArchiveTaskViewModel = ViewModelProviders.of(this).get(ArchiveTaskViewModel.class);
        mStatsViewModel = ViewModelProviders.of(this).get(StatsViewModel.class);
        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(this, new Observer<List<Taskers>>() {
            @Override
            public void onChanged(@Nullable List<Taskers> taskers) {
                if(taskers.size() != 0) {
                    taskViewModel.checkAllDoables(taskers, pref);
                    ((TaskFragment)getParentFragment()).redrawLottie();
                } else {
                    ChartDataHolder.getInstance().setHighTasksDoable(true);
                    ChartDataHolder.getInstance().setMediumTasksDoable(true);
                    ChartDataHolder.getInstance().setAllTasksDoable(true);
                    ChartDataHolder.getInstance().setDeadlinesDoable(true);
                    ((TaskFragment)getParentFragment()).redrawLottie();
                }

                mAdapterAll.submitList(taskers);
                for(int i = 0; i<=taskers.size()-1; i++) {
                    taskers.get(i).getDoableToText();
                }
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

        mAdapterAll = new TaskAdapterAll(getActivity()); /////////////////////////////////////////
        mRecyclerView.setAdapter(mAdapterAll);

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AddEditTaskActivity.class);
                startActivityForResult(i, ADD_TASK_REQUEST);
            }
        });


        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if(direction == ItemTouchHelper.LEFT) {
                    taskViewModel.delete(mAdapterAll.getTaskAt(viewHolder.getAdapterPosition()));
                } else if (direction == ItemTouchHelper.RIGHT) {
                    int expGained = mAdapterAll.getTaskAt(viewHolder.getAdapterPosition()).getExp();
                    int expNextLevel = pref.getInt(NEXT_EXPERIENCE, -1);
                    int currExp = pref.getInt(USER_EXPERIENCE, -1);

                    if(((currExp + expGained)/expNextLevel) > 0) {
                        int oldLevel = pref.getInt(USER_LEVEL, -1);
                        int oldNextExp = pref.getInt(NEXT_EXPERIENCE, -1);
                        pref.edit().putInt(USER_LEVEL, oldLevel + 1).apply();
                        pref.edit().putInt(NEXT_EXPERIENCE, oldNextExp * 2).apply();
                        pref.edit().putInt(USER_EXPERIENCE, currExp + expGained - expNextLevel).apply();
                    } else {
                        pref.edit().putInt(USER_EXPERIENCE, expGained + currExp).apply();
                    }
                    mStatsViewModel.completeTask(mAdapterAll.getTaskAt(viewHolder.getAdapterPosition()));
                    Taskers helpTask = mAdapterAll.getTaskAt(viewHolder.getAdapterPosition());
                    mArchiveTaskViewModel.insert(new ArchivedTasks(helpTask.getName(), helpTask.getDescription(), helpTask.getPriority(), helpTask.getTime_consumption(), helpTask.getD_time_milisec(), new DateHandler().getCurrentDateTimeInMilisec()));
                    taskViewModel.delete(mAdapterAll.getTaskAt(viewHolder.getAdapterPosition()));
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(getActivity(), c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(getActivity(), R.color.PastelRed))
                        .addSwipeLeftActionIcon(R.drawable.ic_delete_black_24dp)
                        .addSwipeLeftLabel("Delete")
                        .setSwipeLeftLabelColor(Color.BLACK)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary))
                        .addSwipeRightActionIcon(R.drawable.ic_check_black_24dp)
                        .addSwipeRightLabel("Complete task")
                        .setSwipeRightLabelColor(Color.BLACK)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);


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
                intent.putExtra(AddEditTaskActivity.EXTRA_COMPLETED, task.getCompleted());
                startActivityForResult(intent, EDIT_TASK_REQUEST);
            }
        });

        FirstRunCheck = pref.getBoolean(HOME_FRAG_RUN, true);
        //if(FirstRunCheck) {
        if(false) {
            pref.edit().putBoolean(HOME_FRAG_RUN, false).apply();
            v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    v.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    final Spotlight spot = Spotlight.with(getActivity())
                            .setOverlayColor(R.color.background)
                            .setDuration(1000L)
                            .setAnimation(new DecelerateInterpolator(2f))
                            .setTargets(buildTargets(v))
                            .setClosedOnTouchedOutside(true)
                            .setOnSpotlightStateListener(new OnSpotlightStateChangedListener() {
                                @Override
                                public void onStarted() {
                                    Toast.makeText(getContext(), "spotlight is started", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onEnded() {
                                    Toast.makeText(getActivity(), "spotlight is ended", Toast.LENGTH_SHORT).show();
                                }
                            });
                    spot.start();
                }

            });
        }

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
            int completed = data.getIntExtra(AddEditTaskActivity.EXTRA_COMPLETED, 0);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(deadline);

            Taskers task = new Taskers(title, description, priority, duration, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) +1, calendar.get(Calendar.YEAR), calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE), deadline.getTime(), 0, completed );

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
            int completed = data.getIntExtra(AddEditTaskActivity.EXTRA_COMPLETED, 0);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(deadline);

            Taskers task = new Taskers(title, description, priority, duration, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) +1, calendar.get(Calendar.YEAR), calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE), deadline.getTime(), 0, completed );
            task.setId(id);

            taskViewModel.update(task);

            Toast.makeText(getActivity(), "Task updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Task not saved", Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<SimpleTarget> buildTargets(View v) {
        ArrayList<SimpleTarget> targets = new ArrayList<>();
        int[] location = new int[2];

        mRecyclerView.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];

        SimpleTarget simpleTarget = new SimpleTarget.Builder(getActivity())
                .setPoint(x + v.getWidth() / 2 , y)
                .setShape(new RoundedRectangle(100f, v.getWidth(), 5f)) // or RoundedRectangle()
                .setDuration(1000L)
                .setTitle("Tasks to do today")
                .setDescription("Here you can see tasks that you should do today, simply swipe in any direction to mark them as completed")
                .setOverlayPoint(mRecyclerView.getX(), mRecyclerView.getY())
                .setOnSpotlightStartedListener(new OnTargetStateChangedListener<SimpleTarget>() {
                    @Override
                    public void onStarted(SimpleTarget target) {
                        // do something
                    }
                    @Override
                    public void onEnded(SimpleTarget target) {
                        // do something
                    }
                })
                .build();
        targets.add(simpleTarget);

        return targets;
    }

}
