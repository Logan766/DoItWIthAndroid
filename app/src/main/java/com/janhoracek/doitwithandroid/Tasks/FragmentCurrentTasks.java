package com.janhoracek.doitwithandroid.Tasks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.janhoracek.doitwithandroid.Data.DataHolder;
import com.janhoracek.doitwithandroid.Data.DateChangeChecker;
import com.janhoracek.doitwithandroid.Data.DateHandler;
import com.janhoracek.doitwithandroid.Database.ArchiveTaskViewModel;
import com.janhoracek.doitwithandroid.Database.ArchivedTasks;
import com.janhoracek.doitwithandroid.Database.StatsViewModel;
import com.janhoracek.doitwithandroid.Database.TaskViewModel;
import com.janhoracek.doitwithandroid.Database.Taskers;
import com.janhoracek.doitwithandroid.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;
/**
 * Fragment contains recycler view for current Tasks
 *
 * @author  Jan Horáček
 * @version 1.0
 * @since   2019-03-28
 */
public class FragmentCurrentTasks extends Fragment {
    public static final int ADD_TASK_REQUEST = 1;
    public static final int EDIT_TASK_REQUEST = 2;
    private static final String PREFS_NAME = "com.janhoracek.doitwithandroid.SettingsSharedPrefs";
    private static final String TASKS_FRAG_RUN = "com.janhoracek.doitwithandroid.TASKS_FRAG_RUN";
    private static final String USER_LEVEL = "com.janhoracek.doitwithandroid.USER_LEVEL";
    private static final String USER_EXPERIENCE = "com.janhoracek.doitwithandroid.USER_EXPERIENCE";
    private static final String NEXT_EXPERIENCE = "com.janhoracek.doitwithandroid.NEXT_EXPERIENCE";

    private FloatingActionButton mFloatingActionButton;
    private TaskViewModel taskViewModel;
    private RecyclerView mRecyclerView;
    private TaskAdapterAll mAdapterAll;
    private StatsViewModel mStatsViewModel;
    private ArchiveTaskViewModel mArchiveTaskViewModel;
    private NestedScrollView mScrollView;
    private SharedPreferences pref;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_tasks_current, container, false);
        pref = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        //check date change
        DateChangeChecker.getInstance().CheckDate(pref);

        mRecyclerView = v.findViewById(R.id.task_fragment_recyclerview);
        mRecyclerView.setItemViewCacheSize(0);
        mFloatingActionButton = v.findViewById(R.id.add_task_fab);

        //load models
        mArchiveTaskViewModel = ViewModelProviders.of(this).get(ArchiveTaskViewModel.class);
        mStatsViewModel = ViewModelProviders.of(this).get(StatsViewModel.class);
        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        //observe changes in database
        taskViewModel.getAllTasks().observe(this, new Observer<List<Taskers>>() {
            @Override
            public void onChanged(@Nullable List<Taskers> taskers) {
                if(taskers.size() != 0) {
                    taskViewModel.checkAllDoables(taskers, pref);
                    ((FragmentTasks)getParentFragment()).redrawLottie();
                } else {
                    DataHolder.getInstance().setHighTasksDoable(true);
                    DataHolder.getInstance().setMediumTasksDoable(true);
                    DataHolder.getInstance().setAllTasksDoable(true);
                    DataHolder.getInstance().setDeadlinesDoable(true);
                    ((FragmentTasks)getParentFragment()).redrawLottie();
                }
                //load tasks
                mAdapterAll.submitList(taskers);

                for(int i = 0; i<=taskers.size()-1; i++) {
                    taskers.get(i).toText();
                }
            }
        });
        //setup RecyclerView
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

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0)
                    mFloatingActionButton.hide();
                else if (dy < 0)
                    mFloatingActionButton.show();
            }
        });

        mAdapterAll.submitList(taskViewModel.getAllTasksList());


        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //delete/complete task
                if(direction == ItemTouchHelper.LEFT) {
                    taskViewModel.delete(mAdapterAll.getTaskAt(viewHolder.getAdapterPosition()));
                } else if (direction == ItemTouchHelper.RIGHT) {
                    completeTask(mAdapterAll.getTaskAt(viewHolder.getAdapterPosition()));
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                //decorate RecyclerView
                new RecyclerViewSwipeDecorator.Builder(getActivity(), c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(getActivity(), R.color.PastelRed))
                        .addSwipeLeftActionIcon(R.drawable.ic_delete_black_24dp)
                        .addSwipeLeftLabel(getString(R.string.task_delete))
                        .setSwipeLeftLabelColor(Color.BLACK)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary))
                        .addSwipeRightActionIcon(R.drawable.ic_check_black_24dp)
                        .addSwipeRightLabel(getString(R.string.complete_task))
                        .setSwipeRightLabelColor(Color.BLACK)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        //edit task
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
        return v;
    }

    /**
     * Completes selected Task
     *
     * @param task Task to be completed
     */
    private void completeTask(Taskers task) {
        int expNextLevel = pref.getInt(NEXT_EXPERIENCE, -1);
        int currExp = pref.getInt(USER_EXPERIENCE, -1);
        int timeConsumption = task.getTime_consumption();
        int completedTime = task.getCompleted();
        int taskExp = task.getExp();
        int expGained;

        if(completedTime >= timeConsumption) {
            expGained = 0;
        } else if (completedTime > 0){
            expGained = Math.round(taskExp * (completedTime/(float) timeConsumption));
        } else {
            expGained = taskExp;
        }


        if(((currExp + expGained)/expNextLevel) > 0) {
            int oldLevel = pref.getInt(USER_LEVEL, -1);
            int oldNextExp = pref.getInt(NEXT_EXPERIENCE, -1);
            pref.edit().putInt(USER_LEVEL, oldLevel + 1).apply();
            pref.edit().putInt(NEXT_EXPERIENCE, oldNextExp * 2).apply();
            pref.edit().putInt(USER_EXPERIENCE, currExp + expGained - expNextLevel).apply();
        } else {
            pref.edit().putInt(USER_EXPERIENCE, expGained + currExp).apply();
        }
        mStatsViewModel.completeTask(task, true);
        mArchiveTaskViewModel.insert(new ArchivedTasks(task.getName(),task.getDescription(), task.getPriority(), task.getTime_consumption(), task.getD_time_milisec(), new DateHandler().getCurrentDateTimeInMilisec()));
        taskViewModel.delete(task);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //add task to databse
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

            Toast.makeText(getActivity(), getString(R.string.fragment_current_task_add_succ), Toast.LENGTH_SHORT).show();
        } else if(requestCode == EDIT_TASK_REQUEST && resultCode == RESULT_OK) {
            //update task in database
            int id = data.getIntExtra(AddEditTaskActivity.EXTRA_ID, -1);

            //catch error
            if(id == -1) {
                Toast.makeText(getActivity(), getString(R.string.fragment_current_task_update_fail), Toast.LENGTH_SHORT).show();
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

            //autocomplete task if completed time is 100% or more
            if(task.getCompleted() >= task.getTime_consumption()) {
                completeTask(task);
                Toast.makeText(getActivity(), getString(R.string.fragment_current_task_over100), Toast.LENGTH_SHORT).show();
            } else {
                taskViewModel.update(task);
                Toast.makeText(getActivity(), getString(R.string.fragment_current_task_update_succ), Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getActivity(), getString(R.string.fragment_current_task_not_saved), Toast.LENGTH_SHORT).show();
        }
    }

}
